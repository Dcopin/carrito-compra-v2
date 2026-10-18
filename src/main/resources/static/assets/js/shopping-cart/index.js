jsBase.nvoEvt( window, 'load', function() {
    jsApp.init( jsApp );
});

let jsApp = function() { let fn = {}; let draw = {};

    fn.init = ( self ) => {
        //jsGen.init();
        
        jsBase.$( 'uiAncAct' )?.on( 'click', function(evt) {
            let e = evt || window.event;
            if( !e.defaultPrevented ) {
                jsBase.addCss( 'loader', 'loader-apa-bck' );
                jsBase.mosDiv( true, 'loader' );
            }
        });

        let objVacCar = jsBase.$( 'ancVaciarCarrito' );

        if( null != objVacCar )
            objVacCar.onclick = function(event){ return jsApp.uifVaciarCarrito( objVacCar, event ); }

        self.prepTodosAncEli();

        //Eliminar todos los "flash mensajes", después de 2 segundos
        new Promise((resolve, reject) => {
            setTimeout(function() {
                jsGen.cerrarTodosLosFlashMensaje();
            }, 2000 );
        });

        /*jsBase.$( 'txtResultado' )?.on( 'keydown paste focus', function(evt) {
            let e = evt || window.event;
            jsGen.textAreaAdjust({ o: e.target });
        });

        jsBase.$( 'btnImportar' )?.on( 'click', function(evt) {
            let e = evt || window.event;
            if( !e.defaultPrevented ) {
                jsBase.addCss( 'loader', 'loader-apa-bck' );
                jsBase.mosDiv( true, 'loader' );
            }
        });

        let objSlct = jsBase.$( 'uiSlctRegPorPag' );
        if( objSlct ) {
            slctL.init( objSlct, '45px', true, true );
            objSlct.on( 'change', self.uifNvoSlct );
        }

        jsGen.prepTodosDataHref();

        jsGen.prepTodosLocHrefAtr({
            strData:'[data-anc="js-pagination"]'
            , tipo: 1
            , fn: 	self.clickLocHref
        });
	
		jsBase.$( 'uiBtnMasResultados' )?.on( 'click', self.uifMasResultados );  
        */
    };

    fn.clickLocHref = function( url ) {
        
        let val = jsBase.optionSel( 'uiSlctRegPorPag' );
        
        if( !(null==val||undefined==val) )
            url += '&regporpag=' + val;

        return url;
    };

    fn.uifMasResultados = function() {

        let objHdNumPaginas = jsBase.$( 'uiHdNumPaginas' );
        let objHdRegPorPag  = jsBase.$( 'uiHdRegPorPag' );
    
        if( !objHdNumPaginas && !objHdNumPaginas ) {
            console.log( 'uiHdNumPaginas && uiHdRegPorPag is null' );
            return;
        }
    
        let numPaginas = objHdNumPaginas.value;
        let regPorPag  = objHdRegPorPag.value;
    
        if( numPaginas.isEmpty() || regPorPag.isEmpty() ) {
            console.log( 'uiHdNumPaginas || uiHdRegPorPag isEmpty' );
            return;	
        }
    
        pagAct++;
        if( pagAct <= numPaginas ){} else {
            //pagAct = pagIni;
            jsBase.mosDiv( false, 'uiBtnMasResultados' );
            //console.log( 'Numero de paginas excedido' );
            return;
        }
    
        let arrPar = {
            esJson: 	 true
            , regporpag: regPorPag
            , nocache: 	 Math.random()
        };
        
        jsGen.ldaData({
            con: 		'tbEmpresas'
            , divAjx: 	'uiDivMasResultadosAjx'
            , divAjx2: 	'uiDivMasResultadosAjx2'
            , lblAjx: 	'Buscando'
            , met: 		'GET'
            , fn:  		jsApp.ldaJsonTblEmpresas
            , url: 		pagAct + '?' + jsBase.param( arrPar )
            , actLoa: 	false 
            , qs: 		null
        });
    };

    fn.prepTodosAncEli = function() {
	
        let objs = document.querySelectorAll( '[data-anc="js-eli"][data-listo="no"]' );
        
        [].forEach.call( objs, ( e ) => {
            e.removeAttribute( 'data-listo' );
            e.onclick = function(event){ return jsApp.uifEliArt( e, event ); }
        });	
    };

    fn.uifEliArt = function( e, event ) {
        let evt = event || window.event;

        evt.preventDefault();
        evt.stopPropagation();
    
        swal({
            text: '\xBFDesea quitar este producto del carrito de compra?',
            icon: 'warning',
            dangerMode: true,
            buttons: {
                cancel: {
                    visible: true,
                    text: 'No, gracias'
                },
                confirm: {
                    visible: true,
                    text: 'Sí, deseo eliminarlo!'
                }
            }
        }).then( ( respuesta ) => {
    
            if( !respuesta ) return false;

            jsBase.addCss( 'loader', 'loader-apa-bck' );
            jsBase.mosDiv( true, 'loader' );
    
            location.href = e.href;
    
        });
    
        jsGen.swalResponsive();
    
        return false;
    };

    fn.uifVaciarCarrito = function( e, event ) {
        let evt = event || window.event;

        evt.preventDefault();
        evt.stopPropagation();
    
        swal({
            text: '\xBFDesea vaciar el carrito de compra?',
            icon: 'warning',
            dangerMode: true,
            buttons: {
                cancel: {
                    visible: true,
                    text: 'No, gracias'
                },
                confirm: {
                    visible: true,
                    text: 'Sí, deseo vaciarlo!'
                }
            }
        }).then( ( respuesta ) => {
    
            if( !respuesta ) return false;

            jsBase.addCss( 'loader', 'loader-apa-bck' );
            jsBase.mosDiv( true, 'loader' );
    
            location.href = e.href;
    
        });
    
        jsGen.swalResponsive();
    
        return false;
    };

    fn.uifNvoSlct = function() {
        let val = jsBase.optionSel( 'uiSlctRegPorPag' );
        
        window.location = '1?regporpag=' + val;
    };

    fn.ldaJsonTblEmpresas = function( objPar, objJson ) {
        
        let objTbdy = $( '#' + objPar.con );
    
        $.each( objJson, function( i, item ) {
    
            objTbdy.append( jsApp.getStrNwTrs( item ) );
    
        });
    
        jsApp.prepTodosAncEli();

        jsGen.prepTodosDataHref();
    
    };

    fn.getStrNwTrs = function( item ) {
        let str = '';
        let href = `empresa/${ item.idEmpresa }`;

        str = jsApp.getDraw().tr({
            dathref: href + '/editar'
            , html: jsApp.getDraw().td({ 
                    datlbl: 'id'
                    , html: jsApp.getDraw().anc({
                        id:         item.idEmpresa
                        , href:     href + '/eliminar'
                        , tit:      'Eliminar'
                        , datanc:   'js-eli'
                        , clss:     'solo-hover'
                        //, style:    'width: 24px; height: 24px;'
                    })
                  })
                + jsApp.getDraw().td({ 
                    datlbl:  'Nombre'
                    , html:  item.nombre
                    , style: 'clear: both;'
                  })
                + jsApp.getDraw().td({ 
                    datlbl:  'País'
                    , html:  item.pais
                  })
        })
        
        return str;
    };

    fn.getDraw = function() {
        return draw;
    };
    
    draw.tr = function(obj) {
        let dathref = (obj.dathref==null)?``:`data-href="${ obj.dathref }" data-listo="no"`;
        let html = (obj.html==null)?'':obj.html;
        return `<tr ${ dathref }>${ html }</tr>`;
    };
    
    draw.td = function(obj) {
        let datlbl = (obj.datlbl==null)?``:`data-label="${ obj.datlbl }"`;
        let html = (obj.html==null)?'':obj.html;
        let style = (null!=obj.style)?`style="${ obj.style }"`:'';
        return `<td ${ datlbl }${ style }>${ html }</td>`;
    };
    
    draw.anc = function(obj) {
        let str = (null!=obj.id)?draw.div({ html: obj.id, clss: 'centrado-porcentual' }):'';
        //let str = (null!=obj.id)?obj.id:'';
        let style = (null!=obj.style)?`style="${ obj.style }"`:'';
        let css = null!=obj.clss?' ' + obj.clss:'';
        
        return `<a href="${ obj.href }" 
                title="${ obj.tit }"
                data-anc="${ obj.datanc }"
                data-listo="no"
                class="anc-cir apa posRel btnTrEliminar${ css }"
                ${ style }>${ str }</a>`;
    };
    
    draw.div = function(obj) {
        let strClss = (obj.clss==null)?``:`class="${ obj.clss }"`;
        let strSty = (obj.sty==null)?``:`style="${ obj.sty }"`;
        return `<div ${ strClss } ${ strSty }>${ obj.html }</div>`;
    };
    
    draw.lbl = function(obj) {
        let forlbl = (obj.forlbl==null)?``:`for="${ obj.forlbl }"`;
        let html = (obj.html==null)?'':obj.html;
        return `<label ${ forlbl }>${ html }</label>`;
    };

    // fn.frmProductoSubmit = function( event ) {
    //     let e = event || window.event;

    //     if( !e.defaultPrevented ) {
            
    //         const frm = new FormData( e.target );

    //         e.preventDefault();
    //         e.stopPropagation();
    //     }
    // };

    return fn;

}();