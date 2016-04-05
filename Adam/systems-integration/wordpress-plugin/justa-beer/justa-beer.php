<?php
/**
* Plugin Name: Justa beer
* Plugin URI: https://justa.beer/
* Description: Smart Buttons for Smart Donations.
* Version: 1.0
* Author: Adam Misiuda
* Author URI: http://
**/

class JustaBeerPlugin {
    const JB_PLUGIN_ID = 'justa-beer';
    const JB_PLUGIN_NAME = 'Justa beer';
    const JB_POST_TYPE = self::JB_PLUGIN_ID;
    const POPUP_PLUGIN_VERSION = '1.0';
    const SELECTED_WIDGET_VARIABLE = 'selected-vidget';
    
    
    public function registerPostType() {
        $name = _x(self::JB_PLUGIN_NAME, self::JB_PLUGIN_ID);
        $addWidget = __('Add widget', self::JB_PLUGIN_ID);
        $args = array(
            'labels' => array(
                // see https://codex.wordpress.org/Function_Reference/register_post_type for available indices
                'menu_name' => self::JB_PLUGIN_NAME,
                'name' => $name,
                'singular_name' => $name,
                'add_new' => $addWidget,
                'add_new_item' => $addWidget,
                'edit_item' => __('Edit widget', self::JB_PLUGIN_ID),
                'new_item' => __('New', self::JB_PLUGIN_ID),
                'all_items' => __('Your widgets', self::JB_PLUGIN_ID),
                'view_item' => __('View widget', self::JB_PLUGIN_ID),
                'search_items' => __('Search widget', self::JB_PLUGIN_ID),
                'not_found' =>  __('No widget found!', self::JB_PLUGIN_ID),
                'not_found_in_trash' => __('No widget found in trash', self::JB_PLUGIN_ID),
                'parent_item_colon' => '',
            ),
            'public' => false,
            'publicly_queryable' => false,
            'show_ui' => true,
            'show_in_menu' => true,
            'query_var' => false,
            'rewrite' => false,
            'capability_type' => 'post',
            'has_archive' => false,
            'hierarchical' => false,
            'menu_position' => null,
            'supports' => array('')
        );
        register_post_type(self::JB_POST_TYPE, $args);
    }
    
    
    public function enqueueStylesAndScripts() {
        wp_enqueue_style(
            'main-styles',
            plugins_url('css/main-styles.css', __FILE__),
            array(),
            self::POPUP_PLUGIN_VERSION);
        wp_enqueue_style(
            'widget-preview-styles',
            plugins_url('widget-preview/styles.css', __FILE__),
            array(),
            self::POPUP_PLUGIN_VERSION);
        wp_enqueue_script('jquery');
        wp_enqueue_script(
            'main-scripts',
            plugins_url('js/main-scripts.js', __FILE__),
            array(),
            self::POPUP_PLUGIN_VERSION);
    }
    
    
    private function printWidgetPreview($name, $selectedName) {
        $class = 'jb-widget-box';
        if ($name === $selectedName) {
            $class .= ' jb-widget-selected';
        }
        echo "<div class='$class' name='$name'>";
        include __DIR__ . '/widget-preview/widget-1.html';
        echo "</div>";
    }
    
    
    public function printWidgetsSelectBox() {
        $selectedName = get_option(self::SELECTED_WIDGET_VARIABLE . get_the_ID(), null);
        $this->printWidgetPreview('w1', $selectedName);
        $this->printWidgetPreview('w2', $selectedName);
        $this->printWidgetPreview('w3', $selectedName);
        $this->printWidgetPreview('w4', $selectedName);
        echo "<input type='hidden' name='jb_selected_widget_box_name' value='$selectedName' />" .
             "<div style='clear:both'></div>" .
             "<script>JustaBeer.initWidgetImgs();</script>";
    }
    
    
    public function printErrorInfoBox() {
        echo "<div class='error-msg'>You can add only one widget at a time! Please go to already created widget and edit it if needed<div>";
    }
    
    
    public function printCustomPublishMetaBox() {
        echo '<div id="major-publishing-actions">';
        $id = get_the_ID();
        $postStatus = get_post_status($id);
        $isPublished = $postStatus === 'publish';
        $deleteLink = get_delete_post_link($id);
        if ($isPublished) {
            echo '<div id="delete-action">' .
                    '<a class="submitdelete deletion" href="' . $deleteLink . '">' .
                        __('Move to Trash', 'wpp') .
                    '</a>' .
		'</div>';
        }
        echo    '<div id="publishing-action">';
        (!$isPublished)
            ? submit_button(__('Create Popup', 'wpp'), 'primary', 'publish', false)
            : submit_button(__('Update Popup', 'wpp'), 'primary', 'submit', false);
        echo    '</div>' .
             '<div class="clear"></div>'.
             '</div>';
    }
    
    
    private function isAddingOrEditingFirstWidget() {
        // @TODO: checkout why get_current_screen() returns null and use it to check page type
        $actionType = filter_input(INPUT_GET, 'action');
        $scriptName = basename(filter_input(INPUT_SERVER, 'SCRIPT_FILENAME'), '.php');
        $isAddingNewPost = $scriptName === 'post-new';
        $isEditingPost = $actionType === 'edit';
        $firstWidgetId = $this->getFirstExistingPostId();
        $isItFirstPost = $firstWidgetId === get_the_ID();
        return ($isAddingNewPost && $firstWidgetId === -1) || ($isEditingPost && $isItFirstPost);
    }
    
    
    public function prepareMetaBoxes() {
        remove_meta_box('submitdiv', self::JB_POST_TYPE, 'side');
        remove_meta_box('slugdiv', self::JB_POST_TYPE, 'normal');
        
        if (!$this->isAddingOrEditingFirstWidget()) {
            $title = __("Cannot add new widget!", self::JB_PLUGIN_ID);
            $contentFun = array($this, 'printErrorInfoBox');
            add_meta_box('wpp_theme_meta_box', $title, $contentFun, self::JB_POST_TYPE, 'normal');
            return;
        }
        $this->addRequiredBoxes();
    }
    
    
    private function addRequiredBoxes() {
        $title = __("Select widget style", self::JB_PLUGIN_ID);
        $contentFun = array($this, 'printWidgetsSelectBox');
        add_meta_box('wpp_theme_meta_box', $title, $contentFun, self::JB_POST_TYPE, 'normal');
        add_meta_box(
            'jb_custom_publish_meta_box',
            __('Save', self::JB_PLUGIN_ID),
            array($this, 'printCustomPublishMetaBox'),
            self::JB_POST_TYPE,
            'side'
        );
    }
    
    
    private function getFirstExistingPostId() {
        $foundId = -1;
        $query = new WP_Query(array(
            'post_type' => self::JB_POST_TYPE
        ));
        if ($query->have_posts()) {
            $query->the_post();
            $postId = get_the_ID();
            $foundId = $postId;
        }
        wp_reset_query();
        return $foundId;
    }
    
    
    public function onPostSave($postId, $post) {
        update_option(
            self::SELECTED_WIDGET_VARIABLE . $postId,
            filter_input(INPUT_POST, 'jb_selected_widget_box_name')
        );
    }
    
    
    public function onPageInit() {
        if (!is_admin()) {
            $this->showRealWidget();
        }
    }
    
    
    public function initWidgetOnPage() {
        wp_enqueue_script(
            'widget-js',
            plugins_url('js/widget.js', __FILE__),
            array(),
            self::POPUP_PLUGIN_VERSION);
    }
    
    
    private function showRealWidget() {
        $widgetId = $this->getFirstExistingPostId();
        $widgetName = get_option(self::SELECTED_WIDGET_VARIABLE . $widgetId, '');
        if (!empty($widgetName)) {
            $scriptUrl = plugins_url('js/widget.js', __FILE__);
            wp_enqueue_script('jquery');
            add_action('wp_footer', array($this, 'initWidgetOnPage'), 11);
        }
    }
    
    public function setCustomPostMessages() {
        return array(
            self::JB_PLUGIN_ID => array(
                0 => '', // Unused. Messages start at index 1.
                1 => __('Widget updated.') . $view_post_link_html,
                2 => __('Custom field updated.'),
                3 => __('Custom field deleted.'),
                4 => __('Widget updated.'),
               /* translators: %s: date and time of the revision */
                5 => isset($_GET['revision']) ? sprintf(__('Widget restored to revision from %s.'), wp_post_revision_title((int) $_GET['revision'], false)) : false,
                6 => __('Widget published.') . $view_post_link_html,
                7 => __('Widget saved.'),
                8 => __('Widget submitted.') . $preview_post_link_html,
                9 => sprintf(__('Widget scheduled for: %s.'), '<strong>' . $scheduled_date . '</strong>') . $scheduled_post_link_html,
               10 => __('Widget draft updated.') . $preview_post_link_html,
            )
        );
    }
}



$plugin = new JustaBeerPlugin();
add_action('init', array($plugin, 'registerPostType'));
add_action('admin_enqueue_scripts', array($plugin, 'enqueueStylesAndScripts'));
add_action('wp_enqueue_scripts', array($plugin, 'enqueueStylesAndScripts'));
add_action('add_meta_boxes', array($plugin, 'prepareMetaBoxes'));
add_action('save_post', array($plugin, 'onPostSave'), 10, 2);
add_action('wp_loaded', array($plugin, 'onPageInit'), 11);
add_filter('post_updated_messages', array($plugin, 'setCustomPostMessages'));