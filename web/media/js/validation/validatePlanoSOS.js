var datos;
$("#planosos").ajaxForm({
    data: {
        oper: 'plano'
    },
    beforeSubmit: function () {
        $('#respuesta').removeAttr('class').addClass('ui-state-active').empty();
        $('<p>', {
            text: 'Estamos procesando el archivo',
            style: 'text-align:center;padding:0;'
        }).appendTo('#respuesta');
    },
    success: mostrarRespuesta,
    error: function () {
        $('#respuesta').removeAttr('class').addClass('ui-state-error').empty();
        $('<pre>', {
            text: 'El archivo no pudo ser procesado',
            style: 'text-align:center;padding:0;'
        }).appendTo('#respuesta');
    }
});

function mostrarRespuesta(data) {
    if (!data[0]) {
        $('#respuesta').removeAttr('class').addClass('ui-state-error').empty();
        $('<pre>', {
            text: [data[1], data[2].errormessage].join("\n\n")
        }).appendTo('#respuesta');
    } else {
        $('#respuesta').removeAttr('class').addClass('ui-state-active').empty();
        $('<pre>', {
            text: [data[1]].join("\n\n"),
            style: 'text-align:center;padding:0;'
        }).appendTo('#respuesta');
        $('<p>', {
            text: ['Registros', data[2].registros].join(": ")
        }).appendTo('#respuesta');
        $('<p>', {
            text: ['Procesados', data[2].procesados].join(": ")
        }).appendTo('#respuesta');
        $('<p>', {
            text: ['Nuevos', data[2].nuevos].join(": ")
        }).appendTo('#respuesta');
        $('<p>', {
            text: ['Errores', data[2].nuevos].join(": ")
        }).appendTo('#respuesta');
        $('<p>', {
            text: ['Actualizados', data[2].actualizados].join(": ")
        }).appendTo('#respuesta');
        $('<p>', {
            text: 'Fechas: ' + [data[2].fechainicio, data[2].fechafin].join(" - ")
        }).appendTo('#respuesta');
        $('<pre>', {
            text: ['Observacion', data[2].observacion].join(": ")
        }).appendTo('#respuesta');
    }
}