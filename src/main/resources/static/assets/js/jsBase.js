HTMLElement.prototype.on = function( eve, fun ) {
    let expresionRegular = /[,;\s]+/;
    let evts = eve.split( expresionRegular );

    for( let i = 0; i < evts.length; i++ ) {

        let evt = evts[i].trim();

        if( evt.isEmpty() ) continue;

		//if( evts.length >= 2 )
		//	console.log( evt );

        if( 'addEventListener' in window ) {

            this.addEventListener( evt, fun, false );

        } else {

            let thisEvent = 'on' + evt;

            if( 'attachEvent' in window )
                this.attachEvent( thisEvent, fun );
            else
                this[ thisEvent ] = fun;
        }
    }

    return this;
}

String.prototype.isEmpty = function() {
    return ( this.length === 0 || !this.trim() );
};

let jsBase = function() {
    let fn = {};
    fn.init = function() {};

    fn.$ = function( strObj ) {

        if( ( typeof strObj === 'string' || strObj instanceof String ) )
            return document.getElementById(strObj);

        return strObj;
    };

    fn.$$ = function( strClss ) {
        return document.querySelector( `.${ strClss }` );
    };

    fn.nvoEvt = function( elemento, evento, funcion ) {
        if( elemento.addEventListener )
            elemento.addEventListener(evento, funcion, false);
        else if( elemento.attachEvent )
            elemento.attachEvent('on' + evento, funcion);
    };

	//--- moviles
	fn.initScrollIrHaciaArriba = function( objPar ) {

		//window.onscroll = function(){ jsBase.scrollIrHaciaArriba( objPar ) };

		jsBase.nvoEvt( window, 'scroll', function(){
			jsBase.scrollIrHaciaArriba( objPar );			
		});

		var objBtnTop = jsBase.$( objPar.id );

		if( objBtnTop ) {

		    objBtnTop.on( 'click', function(evt) {

		        var e = evt || window.event;
		        
		        if( !e.defaultPrevented ) {
		            // Para Safari
		            document.body.scrollTop = 0;

		            // Para Chrome, Firefox, IE y Opera
		            document.documentElement.scrollTop = 0; 
		        }
		    });
		    
		}
	};

	fn.initScrollIrHaciaAbajo = function( objPar ) {

		//window.onscroll = function(){ jsBase.scrollIrHaciaAbajo( objPar ) };

		jsBase.nvoEvt( window, 'scroll', function(){
			jsBase.scrollIrHaciaAbajo( objPar );
		});

		var objBtnTop = jsBase.$( objPar.id );

		if( objBtnTop ) {

		    objBtnTop.on( 'click', function(evt) {

		        var e = evt || window.event;
		        
		        if( !e.defaultPrevented ) {
		            // Para Safari
		            window.scrollTo( 0, document.body.scrollHeight );

		            // Para Chrome, Firefox, IE y Opera
		            window.scrollTo( 0, document.documentElement.scrollHeight );
		        }
		    });
		    
		}
	};

    //--- web
	fn.param = function( object ) {
	    var encodedString = '';
	    for( var prop in object ) {
	        if( object.hasOwnProperty( prop ) ) {
	            if( encodedString.length > 0 ) {
	                encodedString += '&';
	            }
	            encodedString += encodeURI(prop + '=' + object[prop]);
	        }
	    }
	    return encodedString;
	};

	fn.getParameterByName = function( url, name, flg ) {
		if (!url) url = window.location.href;
		name = name.replace(/[\[\]]/g, '\\$&');
		
		var strIni = null, strIniTmp = '[?&]';
		
		if(undefined==flg||null==flg) {
			strIni = strIniTmp;
		} else {
			if(true==flg) {
				strIni = '';
				url = url.replace(/\"/g,'');
			} else if(false==flg)
				strIni = strIniTmp;
		}
		
		var regex = new RegExp( strIni + name + '(=([^&#]*)|&|#|$)'),
			results = regex.exec(url);
		if (!results) return null;
		if (!results[2]) return '';
		return decodeURIComponent(results[2].replace(/\+/g, ' ').trim());
	};

    //--- Css
	fn.hasClass = function( strObj, className ) {
		var element = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;
    	return (' ' + element.className + ' ').indexOf(' ' + className + ' ') > -1;
	};

	fn.setCss = function( strObj, strClass ) {
		var obj = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;
		obj.className = strClass;
	};

	fn.addCss = function( strObj, classToAdd ) {
		var element = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;
		var currentClassValue = element.className;

		if( currentClassValue.indexOf( classToAdd ) == -1 ) {

			if( ( currentClassValue == null ) || ( currentClassValue === '' ) )
				element.className = classToAdd;
			else
				element.className += ' ' + classToAdd;
		}
	};

	fn.remCss = function( strObj, classToRemove ) {
		var element = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;

		var currentClassValue = element.className;

		if( currentClassValue == classToRemove ) {
			element.className = '';
			return;
		}

		var classValues = currentClassValue.split( ' ' );
		var filteredList = [];

		for( var i = 0 ; i < classValues.length; i++ )
			if( classToRemove != classValues[ i ] )
				filteredList.push( classValues[ i ] );

		element.className = filteredList.join( ' ' );
	};

    //--- Mostrar/Ocultar
	//fn.mosDiv = function( strNombre, strDisplay ) {
	fn.mosDiv = function( flg, strNombre ) {
		//alert( 'Revisar fn mosDiv' );
		//console.log( 'Revisar fn mosDiv' );
		this.mostrarDiv( flg, strNombre );
	};

    fn.mostrarDiv = function(flag, strDiv, strCss ) {
		var obj = this.$( strDiv );

		if( typeof this.addCss === 'function' && strCss !== undefined ) {
			if( obj ) {
				if( flag )
					this.addCss( obj, strCss );
				else
					this.remCss( obj, strCss );
					//addCss( obj, strCss );
			}
		} else {
			if( obj )
				obj.style.display = ( flag ) ? 'block' : 'none';
		}
	};

	//--- Texto
	fn.strToBool = function( s ) {
		// will match one and only one of the string 'true','1', or 'on' rerardless
		// of capitalization and regardless off surrounding white-space.
		regex=/^\s*(true|1|on)\s*$/i

		return regex.test(s);
	}

	fn.numToStr = function( num ) {
		
		if(undefined==num||null==num) return '';
		
		var strNum = num.toString();
		
		if(strNum.isEmpty())
			return '';
		
		var x = parseFloat(num);
		
		if(typeof x === "number" && !isNaN(x))
			return x.toString();

		return '';
	}
	
	fn.setTxt = function( strObj, strMsj ) {
		this.setText( strObj, strMsj );
	}

	fn.setText = function( strObj, strMsj ) {
		var tagName = 'ui-desconocido';
		var flg = false;
		var obj = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;
		
		if( !obj ){ console.log( 'No puedo hacer setText sobre "' + strObj + '"' ); return false; }
		
		if(obj.tagName)
			tagName = obj.tagName.toLowerCase();

		if( !obj ) return false;

		if( tagName === 'div' || tagName === 'span' || tagName === 'label' ) {
			obj.innerHTML = strMsj; flg = true;
		} else if( tagName === 'input' || tagName === 'textarea' ) {
			obj.value = strMsj; flg = true;
		} else {
			flg = false;
			console.log( 'Aviso interno(jsBase)[setText(parms: { strObj: "' + strObj + '", strMsj: "' + strMsj + '" } ) ]: tagName "' + tagName + '" no reconocido.' );
		}

		return flg;
	}

	fn.getTxt = function( strObj ) {
		return this.getText(strObj);
	}

	fn.getText = function( strObj ) {
		var strMsj = '';
		var obj = (typeof strObj === 'string' || strObj instanceof String) ? this.$(strObj) : strObj;

		if( null == obj ) { 
			if( typeof strObj === 'string' )
				console.log( 'Error en getTxt/getTxt: ' + strObj ); 
			
			return undefined; 
		}

		var tagName = obj.tagName.toLowerCase();

		if( tagName === 'div' || tagName === 'span' || tagName === 'label' )
			strMsj = obj.innerHTML;
		else if( tagName === 'input' )
			strMsj = obj.value;
		else
			strMsj = obj.value;

		return strMsj;
	}

	//--- Select
	fn.optionSel = function( strSelect, flagText, atributo ){
		try{
			//var objSelect = document.getElementById( strSelect );
			var objSelect = (typeof strSelect === 'string' || strSelect instanceof String) ? this.$(strSelect) : strSelect;
			if( objSelect ) {
				if( undefined == atributo ) {
					if( undefined == flagText || false == flagText )
						return objSelect.options[ objSelect.options.selectedIndex ].value;
					else
						return objSelect.options[ objSelect.options.selectedIndex ].text;
				} else {
					return objSelect.options[ objSelect.options.selectedIndex ].getAttribute( atributo );
				}
			}
			else return null;
		}catch( ex ){ alert( 'Error interno(jsBase)[optionSel(parms)]: ' + ex.message ); }
	};

	fn.selOption = function( strSelect, indice, disabled ){
		try{
			//var objSelect = document.getElementById( strSelect );
			var objSelect = (typeof strSelect === 'string' || strSelect instanceof String) ? this.$(strSelect) : strSelect;
			if( objSelect ){
				try{
					objSelect.options[ indice ].selected = true;
					if( disabled != undefined )
						objSelect.disabled = disabled;
					return true;
				}catch( ex1 ){ return false; }
			}
			return false;
		}catch( ex ){ alert( 'Error interno(jsBase)[selOption(parms)]: ' + ex.message ); }
	}

	fn.selOptionPorValue = function( strSelect, value, disabled ){
		try{
			//var objSelect = document.getElementById( strSelect )
			var flag = false;
			var objSelect = (typeof strSelect === 'string' || strSelect instanceof String) ? this.$(strSelect) : strSelect;
			if( objSelect ){
				try{
					var tam = objSelect.length;
					for( var i = 0; i < tam; i++ ){
						if( objSelect.options[ i ].value == value ){
							objSelect.options[ i ].selected = flag = true;
							break;
						}
					}

					if( disabled != undefined )
						objSelect.disabled = disabled;
					return flag;
				}catch( ex1 ){ return false; }
			}
			return false;
		}catch( ex ){ alert( 'Error interno(jsBase)[selOptionPorValue(parms)]: ' + ex.message ); }
	}

	fn.addItem = function( strSlct, Text, Value, flag ) {
		var opt = document.createElement( 'option' );
		document.getElementById( strSlct ).options.add(opt);
		opt.text = Text;
		opt.value = Value;
		if( flag ) opt.selected = true;
	}

	//--- Miscelánea
	fn.capitalizeFirstLetter = function( string ) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	};

	fn.scrollIrHaciaArriba = function( objPar ) {
		//id, css, top
		var topMax 	= (undefined!==objPar.top)?objPar.top:500;
		var top 	= document.body.scrollTop || document.documentElement.scrollTop;

		if( true === objPar.deb ){
			console.log( top );
		}

		if( top > topMax ) {
		    jsBase.addCss( objPar.id, objPar.css );
		} else {
		    jsBase.remCss( objPar.id, objPar.css );
		}
	};

	fn.scrollIrHaciaAbajo = function( objPar ) {
		//id, css, top
		var topMin	= (undefined!==objPar.top)?objPar.top:500;
		var top 	= document.body.scrollTop||document.documentElement.scrollTop;

		if( true === objPar.deb ){
			console.log( top );
		}
		
		if( top <= topMin ) {
		    jsBase.addCss( objPar.id, objPar.css );
		} else {
		    jsBase.remCss( objPar.id, objPar.css );
		}
	};

	fn.swalResponsive = function() {
		//swal responsive
		$( '.swal-modal' ).addClass( 'swal-modal-js' );
		$( '.swal-footer' ).addClass( 'swal-footer-js' );
		$( '.swal-overlay' ).addClass( 'swal-overlay-js' );
		//fin swal responsive
	};

    return fn;
}();