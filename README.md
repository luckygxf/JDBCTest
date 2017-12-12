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