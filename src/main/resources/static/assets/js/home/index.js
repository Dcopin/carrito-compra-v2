jsBase.nvoEvt( window, 'load', function() {
    jsApp.init( jsApp );
});

let jsApp = function() { let fn = {}; let draw = {};

    fn.init = ( self ) => {

        //jsBase.$( 'uiAncAct' )?.on( 'click', function(evt) {
        //    let e = evt || window.event;
        //    if( !e.defaultPrevented ) {
        //        jsBase.addCss( 'loader', 'loader-apa-bck' );
        //        jsBase.mosDiv( true, 'loader' );
        //    }
        //});

        //Eliminar todos los "flash mensajes", después de 2 segundos
        new Promise((resolve, reject) => {
            setTimeout(function() {
                jsGen.cerrarTodosLosFlashMensaje();
            }, 2000 );
        });
    };

    return fn;

}();