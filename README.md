## 1、名词解释
同源策略: 同源策略是浏览器保护用户安全上网的重要措施，协议、域名、端口号三者相同即为同源。

同站 ：只要两个 URL 的 eTLD+1 相同即可  否则就是跨站 `www.a.com` 和 `www.b.com`

跨域 ：不符合同源策略的就属于跨域,如: `second.a.com` 和 `www.a.com`

注: 跨站一定跨域,跨域不一定跨站

eTLD  :  effective top-level domain(有效顶级域)

PSL :  [公共后缀列表](https://publicsuffix.org/list/public_suffix_list.dat)

Internet 用户可以（或在历史上可以）直接在公共后缀（public suffix）上注册名称。如 `.com, .ci.uk, pvt.k12.ma.us`。（我理解和 eTLD 是一回事）


## 2、跨域解决办法(CORS、JSONP等),这边以CORS为例:
> 以nginx为例:
```
server {
  listen 80;
  server_name www.a.com;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin *;
      proxy_pass http://127.0.0.1:8006/;
  }	
}
```
> 一般这样就能解决跨域了,但是要解决跨域cookie传递,进一步修改为
```
server {
  listen 80;
  server_name www.a.com;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}
```
配合前端传递withCredentials即可
```
$.ajax({
        url: '',
        type: "GET",
        dataType: "json",
        xhrFields: {
            withCredentials: true,
        },
        success: function (data) {

        }
    });
```

## 本地环境搭建

#### 3.1 安装mkcert本地证书
mkcert[下载地址](https://github.com/FiloSottile/mkcert/releases/latest)

进入安装目录,运行
```
.\mkcert-v1.4.4-windows-amd64.exe -install
```
生成证书
```
.\mkcert-v1.4.4-windows-amd64.exe "www.a.com"
.\mkcert-v1.4.4-windows-amd64.exe "www.b.com"
.\mkcert-v1.4.4-windows-amd64.exe "second.a.com"
```
![image-20230817112853872](https://github.com/wtf-boy/cross-origin/assets/11848141/4c8ada29-a272-45e4-8e39-037c5d0730f1)


#### 3.2 本地hosts文件修改
```
127.0.0.1 www.a.com
127.0.0.1 www.b.com
127.0.0.1 second.a.com
```
#### 3.3 nginx 配置 (ssl证书位置相应修改)
```
server {
  listen 80;
  server_name www.a.com;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}

server {
  listen 443;
  server_name www.a.com;
  ssl on;
  ssl_certificate D:\\Software\\mkcert\\www.a.com.pem;
  ssl_certificate_key D:\\Software\\mkcert\\www.a.com-key.pem;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}


server {
  listen 80;
  server_name second.a.com;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}

server {
  listen 443;
  server_name second.a.com;
  ssl on;
  ssl_certificate D:\\Software\\mkcert\\second.a.com.pem;
  ssl_certificate_key D:\\Software\\mkcert\\second.a.com-key.pem;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}		


server {
  listen 80;
  server_name www.b.com;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}

server {
  listen 443;
  server_name www.b.com;
  ssl on;
  ssl_certificate D:\\Software\\mkcert\\www.b.com.pem;
  ssl_certificate_key D:\\Software\\mkcert\\www.b.com-key.pem;
  location / {
      add_header Access-Control-Allow-Methods *;
      add_header Access-Control-Allow-Headers *;
      add_header Access-Control-Allow-Origin $http_origin;
      add_header Access-Control-Allow-Credentials true;
      proxy_pass http://127.0.0.1:8006/;
  }	
}
```

#### 3.4 启动项目,访问成功
![image-20230817113051045](https://github.com/wtf-boy/cross-origin/assets/11848141/885e5607-a294-45ef-8ddf-71868de8293b)



## 结论

同源时cookie的存储与发送没有问题,顶级导航的情况可以看作是同源场景；

不同源场景，若 `withCredentials=false`，则浏览器不会存储cookie；

不同源场景，且`withCredentials=true`，又可分为以下场景：

-  same-site 对于使用HTTPS协议的API，浏览器会存储cookie，不论`samesite`的值； 对于使用HTTP协议的API，浏览器会存储`samesite`的值为`Lax`和`Strict`的cookie； XHR请求会带上目标域的cookie； 
-  cross-site 对于HTTPS协议的API返回的cookie，如果设置了属性：`secure; samesite=none`，则浏览器会存储cookie。XHR请求也会带上目标域的cookie：

## 其它

后端API同时设置`Access-Control-Allow-Credentials`的值为`true`，`Access-Control-Allow-Origin`的值为`*`会报The CORS protocol does not allow specifying a wildcard (any) origin and credentials at the same time. Configure the CORS policy by listing individual origins if credentials needs to be supported.错误。

若前端XHR请求中设置`withCredentials`为`true`，但后台API未设置`Access-Control-Allow-Credentials`，则会报The value of the 'Access-Control-Allow-Credentials' header in the response is '' which must be 'true' when the request's credentials mode is 'include'. The credentials mode of requests initiated by the XMLHttpRequest is controlled by the withCredentials attribute.错误。

若前端XHR请求中设置`withCredentials`为`true`，但后台API配置`Access-Control-Allow-Origin`的值为`*`，则会报The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'. The credentials mode of requests initiated by the XMLHttpRequest is controlled by the withCredentials attribute.错误。




