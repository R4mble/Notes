---
title: Git Flow
date: 2019-04-05 02:33:59
categories: [Git]
---
集中式工作流
    工作方式
        以中央仓库作为项目所有修改的单点实体,所有修改提交到这个分支
        上,只用到master这一个分支.
    冲突解决
        开发者提交之前,需要先fetch中央库的新增提交,rebase自己提交到
        中央仓库提交历史之上.[把自己的修改加到别人已经完成的修改上]
        
    git pull --rebase origin master
        --rebase选项把提交已到同步了中央仓库修改后的master分支的顶部.

功能分支工作流
    所有的功能开发应该在一个专门的分支

GitFlow工作流
    历史分支
        使用两个分支来记录项目的历史,master分支存储了正式发布的历史,develop
        分支作为功能的集成分支.
    功能分支
        使用develop作为父分支,新功能完成时合并回develop分支.
    发布分支
        从develop分支上fork一个发布分支,新建的分支用于开始发布循环,发布分支
        合并到master分支并分配一个版本号打好Tag.

    示例
        git checkout -b some-feature develop

        git status
        git add 
        git commit

        git pull origin develop
        git checkout develop
        git merge some-feature
        git push
        git branch -d some-feature

        git checkout -b release-0.1 develop

        git checkout master
        git merge release-0.1
        git push
        git checkout develop
        git merge release-0.1
        git push
        git branch -d release-0.1

        git tag -a 0.1 -m "Initial public release" master
        git push --tag
        
Forking工作流