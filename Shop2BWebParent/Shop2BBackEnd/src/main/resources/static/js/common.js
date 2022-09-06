$(document).ready(function(){
    $("#logoutLink").on("click",function(e){
        e.preventDefault();
        document.logoutForm.submit();
    });

    customizeDropMenu();
});

function customizeDropMenu(){
    $(".navbar .dropdown").hover(
        function(){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
    },
        function(){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideUp();
    });

    $(".dropdown >a").click(function(){
        location.href = this.href;
    });
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorModal(message){
    showModalDialog("Error", message);
}

function showWarningModal(message){
    showModalDialog("Warning", message);
}