* http://47.96.147.90:8080，例如访问注册接口，那么请求网址为：http://47.96.147.90:8080/user/register
* 返回格式除了自动下载文件没有返回值外，其余格式都是一个json对象，有三个属性：
  * ok：有true和false两个属性，表示操作是否成功
  * message成功或失败信息
  * object：大多数时候都为空，目前有两种情况不为空：请求创建一个分享码，object里面会存储一个字符串类型的分享码；请求查看自己在服务器上的所有音频文件，object里面是一个全部服务器上该用户文件的名字


接口名称 | 访问地址 | 方法 | 参数 | 正确返回格式（json） | 错误返回格式（json）| 备注
---|---|---|---|---|---|---
用户注册| /user/register | POST | username <br/> nickname<br/>password | <small>{"ok": true,"message":"注册成功","object": null}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small> | username和nickname都不允许重复，前台是否可以判断一下这两个值不可以设置中文
用户登录 | /user/login | POST | username<br/>password | <small>{"ok": true,"message":"登录成功","object": null}</small>  | <small>{"ok": false,"message":"错误信息","object": null}</small> | 
用户修改密码 | /user/modify | POST | old<br/>new<br/>(代表新旧密码) | <small>{"ok": true,"message":"修改密码成功","object": null}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small> | 新旧密码允许相同
上传音频文件 | /audio-file/upload | POST | file<br/>(一个音频文件)<br/>name<br/>(这个因为音频文件的名字，可以带后缀名，但是最终的保存格式以file的格式为准) | <small>{"ok": true,"message":"上传成功","object": null}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small> | 最大允许的文件大小为20M，至于允许什么格式之后再具体商议
下载音频文件 | /audio-file/download | POST | name<br/>(必须同上传文件的name值相同) | 若成功，那么自动下载音频文件 | <small>{"ok": false,"message":"错误信息","object": null}</small> | 下咋的音频文件的名字我暂时不确定，具体测试的时候我再看看；下载后服务器仍然保留一份拷贝，不会删除
删除上传过的音频文件 | /audio-file/delete | POST | name<br/>(必须同上传文件的name值相同) | <small>{"ok": true,"message":"删除成功","object": null}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small> | 在服务器上删除文件，但是不会删除分享记录 
查看自己在服务器上的所有文件 | /audio-file/list | GET | | <small>{"ok": true, "message": "查找成功","object": ["music1","little_apple.mp3","ABC"]}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small>
创建分享码 | /share/create | POST | username<br/>(分享给哪个用户)<br/>name(必须同上传文件的name值相同) | <small>{"ok": true,"message":"分享成功","object": "UEhdiUEN8668yigi"}</small> | <small>{"ok": false,"message":"错误信息","object": null}</small> | 不能分享给自己；分享码只能使用一次，暂定有效期为1天内;只能分享自己创建过的而且上传过服务器的文件
用户通过分享码下载 | /share/download | POST | uniqueId<br/>(当初获得的分享码) | 若成功，则自动下载 | <small>{"ok": false,"message":"错误信息","object": null}</small> | 如果分享成功，那么自动在服务器上复制一份一模一样的文件，那么两个用户可以各自修改自己的文件不会有任何影响；用户如果想要知道新文件的名字，那么可以通过搜索接口获得
