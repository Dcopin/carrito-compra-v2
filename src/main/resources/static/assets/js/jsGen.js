let jsGen = function () { let zGbl_DOM_ChangeTimer; let fn = {};

fn.init = function() {
};

fn.swalResponsive = function() {
	//swal responsive
	//$( '.swal-modal' ).addClass( 'swal-modal-js' );
	//$( '.swal-footer' ).addClass( 'swal-footer-js' );
	//$( '.swal-overlay' ).addClass( 'swal-overlay-js' );
	jsBase.addCss( jsBase.$$( 'swal-modal' ) , 'swal-modal-js' );
	jsBase.addCss( jsBase.$$( 'swal-footer' ), 'swal-footer-js' );
	jsBase.addCss( jsBase.$$( 'swal-overlay' ), 'swal-overlay-js' );
	//fin swal responsive
};

//evt: onkeyup
fn.textAreaAdjustV0 = function( parObj ) {
	//console.log('textadjustV0: on');
	let ajustePx = 22;

	parObj.alt = (undefined===parObj.alt)?15:parObj.alt;

	parObj.o.style.height = "1px";
  	//o.style.height = ( 5 + o.scrollHeight ) + 'px';
  	parObj.o.style.setProperty( 'height', ( parObj.alt + parObj.o.scrollHeight - ajustePx ) + 'px', 'important' );
	//console.log('altura: '+parObj.o.style.height);
	if(undefined===parObj.flg||null===parObj.flg||false===parObj.flg)return;
	parObj.o.parentNode.style.height = ( parObj.altPadre + parObj.o.scrollHeight - ajustePx ) + 'px';
	//console.log('padre: on');
};

//evt: onkeydown
fn.textAreaAdjust = function( parObj ){
  let el = parObj.o;
  //console.log('textadjust: on');
  setTimeout(function(){
    //el.style.cssText = 'height: auto; padding:0 !important;';
    // for box-sizing other than "content-box" use:
    // el.style.cssText = '-moz-box-sizing:content-box';
    el.style.cssText = 'height:' + el.scrollHeight + 'px; padding: 0; overflow: hidden;';
  },0);

  if(undefined===parObj.flg||null===parObj.flg)return;
  setTimeout(function(){
    //el.parentNode.style.cssText = 'height:auto; padding:0';
    // for box-sizing other than "content-box" use:
    // el.style.cssText = '-moz-box-sizing:content-box';
    el.parentNode.style.cssText = 'height:' + el.scrollHeight + 'px';
  },0);
};

fn.prepTodosLocHrefAtr = function( objPar ) {

	let objs = document.querySelectorAll( objPar.strData );

	[].forEach.call( objs, ( e )=> {
		if( objPar.tipo == 0 ) {
			e.onclick = function(){ return objPar.fn( e, objPar.strIdExp ); }
		}
		else if( objPar.tipo == 1 ) {
			e.href = objPar.fn( e.href );
		}
		else
			console.log( 'tipo no encontrado > prepTodosLocHrefAtr' );
	});
};

fn.clickLocHref = function( anc, strIdExp ) {
	let url = anc.href;
	let obj = jsBase.$( strIdExp );

	if( !obj ) {
		console.log( 'jsGen: No existe ' + strIdExp );
		return false;
	}

	let idExp = obj.value;

	if( ( null != idExp || undefined != idExp ) && !idExp.isEmpty() ) {

		let tilde = anc.getAttribute( 'data-tilde' );
		let href = url + idExp;

		if( null != tilde && null != anc.title) {
			href += '?frm=' + anc.title.toLowerCase();
		}

		location.href = href;
		//window.location = url + idExp;
		return false;
	} else {

		swal( 'Formulario', 'Seleccione un expediente', 'warning' );
		jsGen.swalResponsive();

	}

	return false;
};

fn.prepTodosLocHrefAtr = function( objPar ) {

	let objs = document.querySelectorAll( objPar.strData );

	[].forEach.call( objs, ( e )=> {
		if( objPar.tipo == 0 ) {
			e.onclick = function(){ return objPar.fn( e, objPar.strIdExp ); }
		}
		else if( objPar.tipo == 1 ) {
			e.href = objPar.fn( e.href );
		}
		else
			console.log( 'tipo no encontrado > prepTodosLocHrefAtr' );
	});
};

fn.setChangeListener = function( target, listener, time ) {
	let tiempo;

	if( null != listener && null != target ) {

		tiempo = ( null == time ) ? 350 : time;

		let observer = new MutationObserver( function( mutations ) {
			mutations.forEach( function( mutation ) {
				jsGen.HandleDOM_ChangeWithDelay( target, listener, tiempo );
		  	});
		});

		const config = {
		  attributes:			false,
		  attributeOldValue:	false,
		  characterData: 		true,
		  characterDataOldValue:true,
		  childList: 			true,
		  subtree: 				true
		};

		observer.observe( target, config );
		//jsBase.nvoEvt( target, 'input', function() { jsGen.HandleDOM_ChangeWithDelay( target, listener, tiempo ); } );
		target.on( 'input', function() {
			jsGen.HandleDOM_ChangeWithDelay( target, listener, tiempo );
		});
	}
};

fn.HandleDOM_ChangeWithDelay = function( objCont, listener, tiempo ) {
    if( typeof jsGen.zGbl_DOM_ChangeTimer == 'number' ) {
        clearTimeout (jsGen.zGbl_DOM_ChangeTimer);
        jsGen.zGbl_DOM_ChangeTimer = '';
    }
    jsGen.zGbl_DOM_ChangeTimer = setTimeout ( listener, tiempo, objCont );
};

fn.uifActPar = function( obj ) {

	obj = jsBase.$( obj );

	let objJQ = $( obj );
	let attr1 = objJQ.attr( 'disabled' );
	let attr2 = objJQ.attr( 'readonly' );

	if ( ( typeof attr1 !== typeof undefined && attr1 !== false ) || ( typeof attr2 !== typeof undefined && attr2 !== false ) ) {
	} else {

		let pat = objJQ.attr( 'pattern' );

		if( undefined===pat||null===pat||pat.isEmpty() )
			jsGen.actPar( objJQ );
		else {

			let regex 	= new RegExp( pat );
			let val 	= objJQ.val();

			if( regex.test( val ) )
				jsGen.actPar( objJQ );
			else {
				let re 	= new RegExp( '[^' + pat + ']' );
				let nval= val.replace( re, '' );

				objJQ.val( nval );
			}
		}
	}
};

fn.actPar = function( objJQ ) { try{
	jsBase.mosDiv( true, 'lblAjax' );
	jsBase.setTxt( 'lblAjax', 'Guardando' );

	let tipo 	= objJQ.attr( 'type' ).toLowerCase();
	let valor 	= objJQ.val();

	if( 'tel' == tipo )
		valor = (objJQ.intlTelInput( 'isValidNumber' )) ? '' + objJQ.intlTelInput( 'getNumber' ) : valor;
	else if( 'checkbox' == tipo )
		//if ( elem.checked )
		//if ( $( elem ).prop( "checked" ) )
		//if ( $( elem ).is( ":checked" ) )
		valor = objJQ.is( ':checked' );
	else if( 'date' == tipo ) {
		let str = '';
		if( !valor.isEmpty() ) {
			let parts = valor.split( '-' );
			let mydate = new Date(parts[0], parts[1] - 1, parts[2]);
			str = mydate.yyyymmdd();
		}
		valor = str;
	}

	let arrPar		= {
    	tbl:		window.atob( objJQ.attr( 'data-tbl' ) )
		, cam:		window.atob( objJQ.attr( 'data-cam' ) )
		, val:		encodeURIComponent( valor )
		, td:		window.atob( objJQ.attr( 'data-td' ) )
		, llave:	window.atob( objJQ.attr( 'data-llave' ) )
		, id:		objJQ.attr( 'data-ihd' )
    	, nocache: 	Math.random()
	};
	//console.log(arrPar);
	//return;
	ajx.mkReq({
		url:	'aut-upd/actualizar-par'
		, met:	'POST'
		, qs:	jsBase.param( arrPar )
	})
	.then( function( objAjax ) {
		jsGen.$actPar( objAjax, arrPar.cam, arrPar.val, objJQ.attr( 'data-ihd' ) );
	}, arrPar.cam, arrPar.val, objJQ.attr( 'data-ihd' ) )
	.catch( function( error ) {
		console.log( error );
	});

}catch( ex ){ alert( 'Error interno(jsGen)[actPar(parms)]: ' + ex.message ); }
};

fn.$actPar = function( objAjax, campo, val, id ) {
try {
	let objRes = null;
	jsBase.setText( 'lblAjax', '' );
	try {
		objRes = JSON.parse( objAjax.responseText );
		if( objRes.error.errno == 0 ) {
			if( objRes.estado == 'ok' ) {
				jsBase.setText( 'lblAjax', 'Guardado' );
				setTimeout( "jsBase.mosDiv( false, 'lblAjax' );", 2000 );
			}
		} else {
			console.log( ':(' + objAjax.responseText );
			jsBase.setText( 'lblAjax', ':( No hay nada que guardar' + jsGen.getStrCerrar( 'lblAjax' ) );
		}
	}catch( ex1 ){ console.log( 'responseText: ' + objAjax.responseText ); console.log( 'ex: ' + ex1.message ); }
}catch( ex ){ alert( 'Error interno(jsGen)[$actPar(parms)]: ' + ex.message ); }
};

fn.ldaData = function( objPar ){
try {

	if( undefined !== objPar.divAjx ) {

		if( undefined !== objPar.divAjx )
			jsBase.mosDiv( true, objPar.divAjx );

		if( undefined !== objPar.divAjx2 )
			jsBase.setTxt( objPar.divAjx2, objPar.lblAjx );
	}

	if( undefined !== objPar.actLoa && true === objPar.actLoa )
		jsBase.mosDiv( true, 'loader' );

	ajx.mkReq({
		url:	objPar.url
		, met:	objPar.met
		, qs:	objPar.qs
	})
	.then( function( objAjax ) {
		jsGen.$ldaData( objAjax, objPar );
	}, objPar )
	.catch( function( error ) {
		console.log( error );
	});

}catch( ex ){ alert( 'Error interno(jsGen)[ldaData(objPar)]: ' + ex.message ); }
};

fn.$ldaData = function( objAjax, objPar ){
try {
	let objRes = null;

	if( undefined !== objPar.divAjx ) {

		if( undefined !== objPar.divAjx )
			jsBase.mosDiv( false, objPar.divAjx );

		if( undefined !== objPar.divAjx2 )
			jsBase.setTxt( objPar.divAjx2, '' );
	}

	if( undefined !== objPar.actLoa && true === objPar.actLoa )
		jsBase.mosDiv( false, 'loader' );

	try {
		if( null != objPar && null != objPar.json && false == objPar.json )
			objRes = objAjax.responseText;
		else
			objRes = JSON.parse( objAjax.responseText );

		if( undefined !== objPar.fn && typeof objPar.fn === 'function' )
			objPar.fn( objPar, objRes );

		if( undefined !== objPar.fnaft && typeof objPar.fnaft === 'function' )
			objPar.fnaft();

	} catch( ex1 ){
		console.log( objAjax.responseText );
		console.log( ex1.message );
		swal( 'Info', objAjax.responseText + '|' + ex1.message, 'info' );
	}

}catch( ex ){ alert( 'Error interno(jsGen)[$ldaData(parms)]: ' + ex.message ); }
};

fn.prepTodosDataHref = function() {
		
	let objs = document.querySelectorAll( 'tr[data-href][data-listo="no"]' );
	
	[].forEach.call( objs, ( e )=> {

		e.removeAttribute( 'data-listo' );
		
		e.onclick = function(evt) {
			
			let e = evt || window.event;
			
			if( !e.defaultPrevented ) {
				jsBase.addCss( 'loader', 'loader-apa-bck' );
				jsBase.mosDiv( true, 'loader' );
			}

			window.location = this.getAttribute( 'data-href' );
			return false;
		}
	});
};

fn.cerrarTodosLosFlashMensaje = function() {

	let objs = document.querySelectorAll( '.divFlashMensaje' );
	[].forEach.call( objs, ( e )=> {
		e.remove();
	});
};

fn.getStrCerrar = function( strDiv ) {
	return '<a href="javascript:jsBase.mostrarDiv( false, \'' + strDiv + '\' );" class="btnLT tooltipX" style="display: inline-block;">'
			+ '<span class="icon-cross1" style=" color: #000; font-size: 13px;"></span>'
			+ '<span class="tooltipX negrilla" style="width: 75px; top: 25px; left: 50px;">Cerrar</span></a>';
};

return fn;

}();