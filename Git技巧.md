---
title: Git技巧
date: 2019-04-05 02:33:59
tags: [Git]
---
git技巧
    放弃本地修改，强制拉取更新   
        git fetch --all                                git fetch 指令是下载远程仓库最新内容，不做合并 
        git reset --hard origin/master                 git reset 指令把HEAD指向master最新版本
        git pull //可以省略



推分支到远程
git push origin local_branch:remote_branch


放弃本地修改
git checkout xxx.java

删除暂存区文件
git rm --cache xxx.zip

git commit -am "xxx" 相当于 git add .  +   git commit -m "xxx"  但是只会add修改的文件

git reset head filename 撤销存入暂存区操作

git log -p -1   来查看最近一次提交的差异，当然也可以使用 git log -p 来查看所有的差异


git commit --amend      使用--amend选项的提交会与最后一次提交进行合并生成一个新的提交，之前的提交会被废弃掉。

git tag -a 版本号 -m 'tag 信息'来创建“轻量标签”

git show v1.1.1 来查看v1.1.1处的相关信息

想给之前的某个commit打一个tag, 那么只需要将commit号追加到打标签的命令后方即可，如：git tag -a vx.x.x -m 'message' commit-hash

 git log --pretty=oneline 命令查看的所有提交

 使用 git push origin --tags 命令将本地创建的所有 tag 推送到远端origin。

 git config --global alias.s status 创建别名

 使用 git config -l 命令来查看你设置过的所有别名

 git log --oneline --graph --all 来查看所有分支情况

 git merge --abort 命令放弃本次合并


 场景： 对于某个git控制下的文件进行了修改，但是改的不满意，想退回到改之前的版本。假定该文件为 src/main/main.c

解决方法：

第一步： 在命令行中输入 git log src/main/main.c 得到该文件的commit 历史。 会得到类似下面的界面



第二步： 复制需要回退版本的hash，在此假设我们回退到 d98a0f565804ba639ba46d6e4295d4f787ff2949 ,则复制该序列即可

第三步：checkout 对应版本。格式为 git checkout <hash> <filename>, 在此即为命令行中输入 git checkout d98a0f565804ba639ba46d6e4295d4f787ff2949 src/main/main.c

第四步： commit checkout下来的版本。 如： git commit -m "revert to previous version"


Git 保存的不是文件差异或者变化量，而只是一系列文件快照。

开发中分支合并最新master且不新增提交:
1.在开发分支新建分支tempbranch,合并最新的master. 使用git read-tree -m -u master
2.切换至master,使用git read-tree -m -u tempbranch, 获得开发改动的代码的 未提交状态.
3.在master新建开发分支