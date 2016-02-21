(function() {
    var paypalEmail = $("#paypalEmail"),
        codeField = $("#generatedCode");
    
    paypalEmail.on("keyup", function() {
        // @TODO: add email validation and tests
        var currentCaret = paypalEmail.caret();
        var email = paypalEmail.val().trim();
        paypalEmail.val(email);
        paypalEmail.caret(currentCaret);
        var code = (email === "")
            ? ""
            : generateWidgetCode(email);
        codeField.val(code);
    });
    
    
    function generateWidgetCode(email) {
        return "<script>document.write(\"<script type='text/javascript' src='js/widget.js?z=\" + Date.now() + \"&receiver_paypal_email=" + email + "'><\\/script>\");</script>";
    }
})();