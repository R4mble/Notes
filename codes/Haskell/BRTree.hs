-- 根节点是黑色的
-- 红色节点不能为父子
-- 从任意节点到每个Nil节点路径上黑色节点数量相同

data RBTree a = Nil | Node Color a (RBTree a) (RBTree a) deriving (Show)
data Color = Red | Black deriving (Show, Eq)

-- 黑化
blacken Nil = Nil
blacken (Node _ value left right) = Node Black value left right

-- 二叉树的方式递归插入节点x
insert x root = blacken $ insert' root
    where insert' Nil = Node Red x Nil Nil
          insert' root@(Node color y left right)
              | x < y = balance color y (insert' left) right
              | x > y = balance color y left (insert' right)
              | otherwise = root

-- 左左,左右,右左,右右 四种情况最后旋转出的结果一样, 第五个模式覆盖了没有红黑矛盾的情况
balance Black z (Node Red x a (Node Red y b c)) d = Node Red y (Node Black x a b) (Node Black z c d)
balance Black z (Node Red y (Node Red x a b) c) d = Node Red y (Node Black x a b) (Node Black z c d)
balance Black x a (Node Red y b (Node Red z c d)) = Node Red y (Node Black x a b) (Node Black z c d)
balance Black x a (Node Red z (Node Red y b c) d) = Node Red y (Node Black x a b) (Node Black z c d)
balance color value left right = Node color value left right

-- 删除N:
-- 如果N只有一个孩子, 那它一定是红色,而N一定是黑色的. 因此让孩子取代N即可.
-- 如果N没有孩子, N非红即黑
--      N为红: 直接删掉, 不影响黑高
--      N为黑:
isBlack (Node Red _ _ _) = False
isBlack _ = True

balL color y (left, True) right = (Node color y left right, True)
balL color y (left, False) right = balL' color y left right

balR color y left (right, True) = (Node color y left right, True)
balR color y left (right, False) = balR' color y left right

balL' color1 p n (Node color2 s sl sr)
    | color2 == Red = balL Black s (balL' Red p n sl) sr
    | isBlack sl && isBlack sr = (Node Black p n (Node Red s sl sr), color1 == Red)
    | not (isBlack sr) = (Node color1 s (Node Black p n sl) (blacken sr), True)
    | otherwise = let (Node Red x sll slr) = sl in balL' color1 p n (Node Black x sll (Node Red s slr sr))

balR' color1 p (Node color2 s sl sr) n
    | color2 == Red = balR Black s sl (balR' Red p sr n)
    | isBlack sl && isBlack sr = (Node Black p (Node Red s sl sr) n, color1 == Red)
    | not (isBlack sl) = (Node color1 s (blacken sl) (Node Black p sr n), True)
    | otherwise = let (Node Red x srl srr) = sr in balR' color1 p (Node Black x (Node Red s sl srl) srr) n

delete x t = fst $ delete' x t
  where delete' x Nil = (Nil, True)
        delete' x root@(Node color y left right)
            | x < y = balL color y (delete' x left) right
            | x > y = balR color y left (delete' x right)
            | otherwise = deleteRoot root
        deleteRoot (Node color _ Nil Nil) = (Nil, color == Red)
        deleteRoot (Node _ _ left Nil) = (blacken left, True)
        deleteRoot (Node _ _ Nil right) = (blacken right, True)
        deleteRoot (Node color _ left right) = let m = findMin right in balR color m left (delete' m right)
        findMin (Node _ x Nil _) = x
        findMin (Node _ _ left _) = findMin left

