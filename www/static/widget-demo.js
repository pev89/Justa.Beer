(function() {
    var body = $("body"),
        formContainer = $("#formContainer"),
        urlInput = $("#userPageUrl");
    

    /* Setting listeners to the form elements */
    $("#formSubmitButton").on("click", onFormSubmit);
    urlInput.on("keypress", function(e) {
        if (e.keyCode === 13) {
            onFormSubmit();
        }
    });
    
    
    function onFormSubmit() {
        var userUrl = urlInput.val().trim();
        if (userUrl === "") {
            showErrorMessage("Please provide a URL!");
            return;
        }
        if (!isUrlValid(userUrl)) {
            showErrorMessage("Given URL is not valid!");
            return;
        }
        showDemoPage(userUrl);
    }
    
    
    function showErrorMessage(msg) {
        var msgContainer = $(
                "<div id=\"msgContainer\">" +
                    "<div class=\"bg\"></div>" +
                    "<div class=\"box\">" +
                        "<div class=\"msg\">" + msg + "</div>" +
                        "<button>Close</button>" +
                    "</div>" +
                "</div>");
        msgContainer.find("button").on("click", function() {
            msgContainer.remove();
            msgContainer = undefined;
        });
        body.append(msgContainer);
    }
    
    
    function isUrlValid(url) {
        // TODO: implements validation and add tests when its ready
        // TODO: add tests for URL validation
        return true;
    }
    
    
    function showDemoPage(userUrl) {
        formContainer.hide();
        var iframe = $("<iframe src=\"" + userUrl + "\"></iframe>"),
            iframeCloseButton = $("<div id=\"iframeCloseButton\">" +
                "<div class=\"bg\"></div>" +
                "<div class=\"arrow\"></div>" +
            "</div>");
        iframeCloseButton.on("click", closeDemoPage);
        iframe.on("load", function(e) {
            console.log(e);
            console.log(Object.keys(this.contentWindow));
            addWidgetScript();
        });
        body.append(iframe);
        body.append(iframeCloseButton);
        window.location.hash = "show";
        // TODO: detect when site has same domain policy (like google) and write tests and also if exists
    }
    
    
    function addWidgetScript() {
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.charset = 'utf-8';
        script.defer = true;
        script.async = true;
        body[0].appendChild(script);
        script.src = 'static/widget.js';
    }
    
    
    function closeDemoPage() {
        $("#iframeCloseButton").remove();
        $("iframe").remove();
        $(".jsb-widget").remove();
        formContainer.show();
        window.location.hash = "";
    }
    
    
    window.onhashchange = function() {
        if (window.location.hash === "") {
            closeDemoPage();
        }
    };
})();