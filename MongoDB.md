集合
    创建集合
        db.createColleaction(name, options)
            name: 集合名称
            options:
                capped: 是否创建固定大小集合,当达到最大值时,自动覆盖最早的文档
                size: 固定集合字节大小
                antoIndexId: 是否自动在_id字段创建索引.默认为false
                max: 固定集合中包含文档的最大数量
            插入文档时,先检查size再检查max
    删除集合
        db.col.drop()
    
    插入文档
        db.col.insert(document)
        例:
            db.col.insert({
                title: 'MongoDB 教程', 
                tags: ['mongodb', 'database', 'NoSQL'],
                likes: 100
            })

    查看文档
        db.col.find()

    更新文档
        db.col.update(
            <query>,
            <update>,
            {
                upsert: <boolean>,
                multi: <boolean>,
                writeConcern: <document>
            }
        )

        query : update的查询条件
        update : update的对象和一些更新的操作符（如$,$inc...）
        upsert : 可选，这个参数的意思是，如果不存在update的记录，是否插入,默认是false不插入
        multi : 可选，mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
        writeConcern :可选，抛出异常的级别。

        例: 
            db.col.update({title: 'MongoDB教程'}, {$set: {title: 'MongoDB'}})
            要修改多条参数:
            db.col.update({title: 'MongoDB教程'}, {$set: {title: 'MongoDB'}}, multi: true)