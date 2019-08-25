---
title: Git技巧
date: 2019-04-05 02:33:59
tags: [Git]
categories: [Git]
---
```
git技巧

后悔药系列:
    撤销存入暂存区操作
    git reset head <filename> 

    删除暂存区文件
    git rm --cache xxx.zip
    
    放弃本地修改
    git checkout xxx.java

    命令放弃本次合并
    git merge --abort 

    退回到改之前的版本
    git checkout <hash> <filename>

    放弃本地修改，强制拉取更新   
    git fetch --all                                git fetch 指令是下载远程仓库最新内容，不做合并 
    git reset --hard origin/master                 git reset 指令把HEAD指向master最新版本

提交:
    相当于git add . 加上 git commit -m "xxx"  只会add修改的文件,不add新增的文件.
    git commit -am "xxx" 

    提交会与上一次提交合并成一个新的提交，之前的提交会被废弃掉。
    git commit --amend  

    放弃commit, 回到未提交状态
    git reset --soft HEAD^

    git reset --hard xxxx

推分支到远程
    git push origin local_branch:remote_branch
删除远程分支
    git push origin --delete remote_branch

查看历史:
    来查看最近一次提交的差异，当然也可以使用 git log -p 来查看所有的差异
    git log -p -1   

    git log --pretty=oneline 
    git log --oneline --graph --all 
    
标签:
    git tag -a 版本号 -m 'tag 信息'来创建“轻量标签”

    git show v1.1.1 来查看v1.1.1处的相关信息

    给之前的某个commit打一个tag, 将commit号追加到打标签的命令后方
    git tag -a vx.x.x -m 'message' commit-hash

    将本地创建的所有 tag 推送到远端origin
    git push origin --tags

别名:
    创建别名
    git config --global alias.s status 

    查看设置过的所有别名
    git config -l

开发中分支合并最新master且不新增提交:
1.在开发分支合并最新的master.
2.切换至master,使用git read-tree -m -u <开发分支>, 获得开发改动的代码的 未提交状态.
3.在master新建开发分支

理论知识:
    Git 保存的不是文件差异或者变化量，而只是一系列文件快照。

    git add时, 暂存操作会为每个文件计算校验和,把当前版本的文件使用bolb对象保存到Git仓库中.

    git commit时, 提交操作会计算每一个子目录的校验和, 在Git仓库中将这些校验和
    保存为树对象. 然后Git创建一个提交对象, 包含一个指向暂存内容快照的指针,作者的
    姓名邮箱,提交时的输入信息和指向父对象的指针, 指向树对象的指针.

        此时Git仓库中的对象:
            bolb对象: 保存文件快照.
            树对象: 记录着目录结构和blob对象索引.
            提交对象: 包含着指向前面树对象的指针和所有的提交信息.

    Git分支: 指向提交对象的可变指针. 实质上是包含所指向对象校验和的文件. 
    创建分支: 创建一个可以移动的新指针. 相当于往一个文件中0  写41个字节(40个SHA-1字符和1个换行符).
    HEAD指针: 指向当前所在的分支

Fast-forward
    如果顺着一个分支走下去能够达到另一个分支, 那么Git在合并时只会简单的将指针前进.
'recursive' strategy 合并提交
    选取一个共同祖先, 作为合并的基础, 做一个新的快照并自动创建一个提交指向它.

git rebase
    将提交到某一分支上的所有修改都应用到另一分支.
    原理: 先找到最近共同祖先, 对比当前分支相对于该祖先的历次提交,
        提取相应的修改并存为临时文件,然后将当前分支指向目标基底,
        将之前另存为临时文件的修改依序应用.
    实质: 丢弃一些现有的提交, 然后相应地新建一些内容一样但不同的提交.
    结果: 最终结果和git merge没有任何区别, 只是让提交历史变成一串.
    特殊选项: 
        --onto
            取出 client 分支，找出处于 client 分支和 server 分支的共同祖先之后的修改，然后把它们在 master 分支上重放一遍
            git rebase --onto master server client
        
        将特性分支变基到目标分支
        git rebase [basebranch] [topicbranch]

    风险: 丢弃掉已经被人依赖的提交, 导致这边丢不掉, 那边提交重复.
    解决办法: 用rebase解决rebase.
            git rebase teamone/master
        过程:1. 检查哪些提交是我们分支独有的.
            2. 检查其中哪些提交不是合并操作的结果.
            3. 检查其中哪些提交在对方覆盖更新时并没有被纳入目标分支.
            4. 把查到的这些提交应用到teamone/master上.
            
    原则: 只对尚未推送或分享给别人的本地修改执行rebase操作清理历史,
         不对已推送至别处的提交执行rebase.

```