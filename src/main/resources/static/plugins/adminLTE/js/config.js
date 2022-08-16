/*
 * @Descripttion: 
 * @version: 
 * @Author: chenxu
 * @Date: 2021-02-18 10:21:22
 * @LastEditors: chenxu
 * @LastEditTime: 2021-03-08 16:05:25
 */
//axios封装post请求
function axiosPostRequst(base_url, url, data, token) {
  let rolegroupId,staffId,secretId,secretkey,nonceStr,signDate,timestamp;
  if(window.localStorage){
    if(localStorage.getItem("ngs_token") !== null){
      token = localStorage.getItem("ngs_token");
    }
    staffId = localStorage.getItem("ngs_staffid");
    rolegroupId = localStorage.getItem("ngs_rolegroupid");
    secretId = localStorage.getItem("ngs_secret_id");
    secretkey = localStorage.getItem("ngs_secret_key");
    nonceStr = Math.random().toString(12).substr(3);
    timestamp = new Date().getTime();
    signDate = "data=" + JSON.stringify(data) + "&noncestr=" + nonceStr + "&rolegroupid=" + rolegroupId + "&secretid=" + secretId + "&staffid=" + staffId + "&timestamp=" + timestamp + "&token=" + token + secretkey;
  }

  
  var result = axios({
    method: 'post',
    url: base_url + url,
    data: data,
    headers: {
		'Content-Type':'application/json',
        'staffid':staffId,
        'rolegroupid':rolegroupId,
        'token':token,
        'secretid':secretId,
        'noncestr':nonceStr,
        'sign': md5(signDate),
        'timestamp': timestamp
    }
  }).then(function (resp) {
    return resp.data;
  }).catch(function (error) {
    return {code:400};
  });
  return result;
}

//get请求
function axiosGetRequst(base_url, url, data, token) {
  if(window.localStorage && localStorage.getItem("ngs_token") !== null){
    token = localStorage.getItem("ngs_token");
  }
  var result = axios({
    method: 'get',
    url: base_url + url,
    params: data,
	headers: {
	  'Content-Type':'application/json',
	  'token':token,
    }
  }).then(function (resp) {
    return resp.data;
  }).catch(function (error) {
    return "exception=" + error;
  });
  return result;
}


//get for export请求
function axiosExportRequst(base_url, url, data, token) {
  if(window.localStorage && localStorage.getItem("ngs_token") !== null){
    token = localStorage.getItem("ngs_token");
  }
  var result = axios({
    method: 'get',
    url: base_url + url,
    params: data,
	responseType: 'blob',
	headers: {
	  'Content-Type':'application/json',
	  'token':token,
    }
  }).then(function (resp) {
    return resp.data;
  }).catch(function (error) {
    return "exception=" + error;
  });
  return result;
}
/**
 * 普通日期格式化
 * @param {*} time 需要格式化的日期
 * @param {*} format 需要转换成的时间格式 'yyyy-MM-dd HH:mm:ss' / 'yyyy-MM-dd'
 */
function timeFormat(time, format) {
  var _time = time
  if (!coerceBoolean(_time)) return ''
  if (/^\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}(\.0)?$/g.test(_time)) { // ‘2018-08-21 15:28:00’这种格式ie浏览器不兼容
    _time = _time.replace(/-/g, '/')
    _time = _time.replace(/(\.0)?$/i, '')
  }
  var t = new Date(_time)
  var y = t.getFullYear()
  var M = t.getMonth() + 1
  if (M < 10) M = '0' + M
  var d = t.getDate()
  if (d < 10) d = '0' + d
  var H = t.getHours()
  if (H < 10) H = '0' + H
  var m = t.getMinutes()
  if (m < 10) m = '0' + m
  var s = t.getSeconds()
  if (s < 10) s = '0' + s
  return format.replace(/yyyy/, y).replace(/MM/, M).replace(/dd/, d).replace(/HH/, H).replace(/mm/, m).replace(/ss/, s)
}


function coerceBoolean(value) {
  return value !== null && value !== undefined && value !== 'false' && value !== 'NaN' && value !== ''
}
