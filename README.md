# web项目通用配置
1. 全局返回数据结构封装
```json
{
  "success": true,
  "code": "0000",
  "msg": "操作成功",
  "data": null // obj or list
}
```

2. 返回值序列化
```
1. Long -> String
2. BigDecimal -> String
3. Date -> yyyy-MM-dd HH:mm:ss
```

3. 全局异常捕获和通用异常类封装
```
可继承BusinessException类扩展自定义异常
```

4. xss过滤器
```yaml
xss:
  request:
    enabled: true # 默认false
    patterns:
      -
    excludes:
      -
  response:
    enabled: true # 默认false
```

5. 特殊类型请求参数自动绑定
```
1. Date类型
2. 枚举类型，实现BaseEnum接口
```

6. logback默认配置
7. 参数验证通用分组