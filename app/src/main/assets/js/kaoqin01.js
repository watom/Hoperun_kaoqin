function init(){
	    document.getElementById('login_id').focus();
        var errors = '';
        if(errors.length > 0){
        	var warning = document.getElementById("ecm");
  		    warning.innerHTML = errors;
  		    document.getElementById('password').focus();
        }
    }

	function doCheck(arg01,arg02){
   		var flag = false;
   		var warning = document.getElementById("ecm");
   		var userid="";
   		var userpwd="";
   		if(arg01 && arg02){
		 document.getElementById("login_id").value=arg01;
		 document.getElementById("password").value=arg02;
		}else{
   		 userid = document.getElementById("login_id").value;
   	     userpwd = document.getElementById("password").value;
   		}
		warning.innerHTML = "";
   		var regu = /^[0-9]+$/;
	    var re = new RegExp(regu);
	    if ("" == userid) {
	        warning.innerHTML = "工号不能为空，请您输入！";
	        document.getElementById('login_id').focus();
	    } else if (!re.test(userid)) {
	        warning.innerHTML = "工号必须为0-9的数字，请您重新输入！";
	        document.getElementById("login_id").value='';
	        document.getElementById('login_id').focus();
   		} else if (9 < userid.length) {
   		    warning.innerHTML = "工号不能多于9位数字，请您输入！";
   		    document.getElementById("login_id").value='';
   		    document.getElementById('login_id').focus();
   		} else if ("" == userpwd) {
   		    warning.innerHTML = "密码不能为空，请您输入！";
   		    document.getElementById('password').focus();
   		} else if (6 > userpwd.length) {
   		    warning.innerHTML = "密码至少要6位，请您输入！";
   		    document.getElementById('password').focus();
   		}  else {
   		    flag = true;
   		}
   		return flag;
   	}

    //禁止输入字母和特殊字符
 	function blockChar(oText){
	   testKey();
		sChar = oText.getAttribute("validchar");
		ddd = String.fromCharCode(window.event.keyCode);
		var res = sChar.indexOf(ddd) > -1;
		return res || window.event.ctrlKey;
	}
	//enter提交
	function testKey(){
		var ev=window.event.keyCode;
		if(ev == 13){
		    doCheck();
	   }
	}
    //安卓调用JS的接口
	function androidLoginInterface(id,password){
	      doCheck(id,password);
	   	}