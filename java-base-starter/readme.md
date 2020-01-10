## java项目通用的配置

### 1 开始使用

#### 1-1 引入:
```
<dependency>
    <groupId>cn.lishe</groupId>
    <artifactId>java-base-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
#### 1-2 配置文件
```$xslt
#===========================================
#  datasource
#===========================================
db:
  driver-class-name: com.mysql.cj.jdbc.Driver
  username: root
  password: root
  url: jdbc:mysql://192.168.1.10:3306/monitor?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai

#===========================================
#  redis
#===========================================
spring:
  redis:
    host: 127.0.0.1
    port: 6379

#===========================================
#   auth 配置
#===========================================
auth:
  jwt-token:
    header-name: Authorization #token 在请求头中的key
    request-name: authorization  #token 在请求参数中的key
    secret: lsidk210398$od$12so #token 加密秘钥
    exclusive-path: /user/tokenLogin,/user/register,/error,/task/**,/favicon.ico,/user/list   #不校验token的url
  sign-token:
    header-name: token #sign 在请求头中的key
    request-name: token #sign 在请求参数中的key
    secret: lishe_md5_key_56e057f20f883e # sign校验的秘钥
    include-path: /user/list # sign校验的url
```

一些说明: 此项目是基于springboot mybatisplus security reids, 
实现了数据源datasource的配置;
登录认证采用redis token的形式, 故redis需要配置;
以及一些权限的配置


#### 1-3 启动类
```
Ctx.CONTEXT = SpringApplication.run(SaasApi.class, args);
```
#### 1-4 需要实现UserDetailService接口
#### 1-5 使用权限校验,需要登录成功之后需要向redis存入userSession,调用UserSession的静态方法saveUserSession即可


### 2 枚举json序列化返回
实现接口IJsonValue

### 3 异常处理

### 4 参数校验
in phoneNo username

### 5 全局返回工具类 R
若Re枚举不足, 可以自己继承ResultCode类

### 6 sign 值
**sign值获取**

使用MD5加密，参数加密规则如下：
	1. 请求的所有参数，需要根据参数名=参数值的格式，按首字符字典顺序（ascii值大小）排序，若遇到相同首字符，则判断第二个字符，以此类推，待签名字符串需要以“参数名1=参数值1&参数名2=参数值2&….&参数名N=参数值N”的规则进行拼接。再加密钥进行md5
	例如：a=1&b=2key
	2.在对请求的参数做签名时，这些参数必须来源于请求参数列表，并且除去列表中的参数sign。
	3.在对请求的参数做签名时，对于请求参数列表中那些可空的参数，如果选择使用它们，那么这些参数的参数值必须不能为空或空值。
	4. 参数编码为utf-8
	5. 密钥：key=lishe_md5_key_56e057f20f883e

例如:
	1.  有三个参数如下:
	phone_num: 13000000958
	nonce: a4sfdswerfag7asdf7
	md5_sign: dd74ce08fbc5b8c7958887275836957d

	2. 排序并组装得到如下参数串(postSortStr):
	md5_sign=dd74ce08fbc5b8c7958887275836957d&nonce=a4sfdswerfag7asdf7&phone_num=13000000958

	3. 获取sign值(postSortStr是第二步得到的字符串):
	sign = MD5(postSortStr + key)
	
 **自定义sign值加密方式**
 
 实现SignGenerator接口

