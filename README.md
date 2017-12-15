# 说明
1. jdbc使用mysql数据库事务demo
## 实现
1. 设置Connection.setAutocommit(false);不自动提交事务    
2. 手动提交事务connectin.commit();   
3. 回滚事务connection.rollback();   
## demo说明
1. 先更新一条记录，在插入一条已有记录  
2. 事务操作，会进行回滚，update不成功  
3. 非事务操作， 不会进行回滚， update成功
## 总结
事务可以保证数据一致性
## JUnit
使用junit单元测试
## 事务隔离级别设置
1. 通过connection.setTranactionLevel(Connection.Tran...)  
2. demo列子，读未提交，可以读到没有提交的变量  
3. 读已提交，不能读到已提交变量内容  
4. 隔离级别越强，性能损失越严重
## 功能
1. 使用Callabletatment调用mysql存储过程
2. 也可以使用preparementstatment访问mysql存储过程 | 存储过程是存放在db server端可执行的sql，因此可以用preparementstatment访问