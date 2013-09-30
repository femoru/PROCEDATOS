

function Generar() {
    if ($("#login").val() != "") {
        $.blockUI({
            message: '<h2>Iniciando Sesion.....</h2>',
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .5,
                color: '#fff'
            }
        });
        setTimeout($.unblockUI, 50000);
    }
}

