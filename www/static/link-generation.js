(function() {
    var userUrl = $("#userUrl"),
        codeField = $("#generatedCode");
    
    userUrl.on("keyup", function() {
        var currentCaret = userUrl.caret();
        var url = userUrl.val().trim();
        userUrl.val(url);
        userUrl.caret(currentCaret);
        var code = (url === "")
            ? ""
            : generateWidgetCode(url);
        codeField.val(code);
    });
    
    
    function generateWidgetCode(url) {
        return "<script>document.write(\"<script type='text/javascript' src='js/widget.js?z=\" + Date.now() + \"&url=" + url + "'><\\/script>\");</script>";
    }
})();