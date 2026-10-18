var ajx = function () {
	
var FF = navigator.userAgent.toLowerCase().indexOf('firefox/') > -1;
	
var READY_STATE_UNINITIALIZED	= 0;
var READY_STATE_LOADING			= 1;
var READY_STATE_LOADED			= 2;
var READY_STATE_INTERACTIVE		= 3;
var READY_STATE_COMPLETE		= 4;
		
var fn = {};

fn.init = function() {
};

fn.iniXhr = function() {
	var xmlhttp;
	try {
		xmlhttp = new ActiveXObject( 'Msxml2.XMLHTTP' );
	}
	catch( e ) {
		try {//codigo para IE6, IE5
			xmlhttp = new ActiveXObject( 'Microsoft.XMLHTTP' );
		}
		catch( E ){
			xmlhttp = false;
		}
	}
	
	//codigo para IE7+, Firefox, Chrome, Opera, Safari
	if( !xmlhttp && typeof XMLHttpRequest != 'undefined' )	
		xmlhttp = new XMLHttpRequest();
	
	return xmlhttp;
};

fn.mkReq = function( par ) {

	// Create the XHR request
	var request = ajx.iniXhr();

	// Return it as a Promise
	return new Promise( function( resolve, reject ) {
		
		// Setup our listener to process compeleted requests
		request.onreadystatechange = function () {

			// Only run if the request is complete
			if( request.readyState !== READY_STATE_COMPLETE ) return;

			// Process the response
			if( request.status >= 200 && request.status < 300 ) {
				// If successful
				resolve( request );
			} else {
				// If failed
				reject({
					status: request.status,
					statusText: request.statusText
				});
			}
			
		};

		// Setup our HTTP request
		request.open( par.met, par.url, true );

		if( par.met == 'POST' )
			request.setRequestHeader( 'Content-Type', 'application/x-www-form-urlencoded' );

		// Send the request
		request.send( par.qs );

	});
};

fn.moLblAjx = function( objPar ) {
	
	const TIEMPO_DEFAULT = 1000;

	if( true == objPar.flg ) {

		if( null != objPar.id ) {
			jsBase.mosDiv( true, objPar.id );
			jsBase.setTxt( objPar.id, objPar.msj || '' );
		}

		if( null != objPar.id ){
			let valorPromesa = new Promise( (resolve) => {
			    setTimeout( () => {
			    	
			    	if( null != objPar.fn )
			    		resolve( objPar.param || null );

			    	jsBase.mosDiv( false, objPar.id );
			    }, objPar.tpo || TIEMPO_DEFAULT );
			});

			if( null != objPar.fn )
				valorPromesa.then( function(data) {
					objPar.fn(data);
				});
		}

	} else if( false == objPar.flg ) {

		if( null != objPar.error && true == objPar.error.swal )
			swal( objPar.error.tit || '', objPar.error.msj || '', objPar.error.tip || 'info' );

		if( null != objPar.error && true == objPar.error.resp )
			jsGen.swalResponsive();
	}
};

return fn;
}();