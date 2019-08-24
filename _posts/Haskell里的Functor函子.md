---
title: Haskell里的Functor函子
date: 2019-04-04 20:41:22
tags: [Haskell, Functor, 函数式编程]
---

```haskell
import Data.List
import Data.Char
main = do line <- fmap (intersperse '-' . reverse . map toUpper) getLine
          putStrLn line
          main
```          