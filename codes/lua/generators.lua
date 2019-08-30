local f = require 'fun'
-- Lua函数式库测试

-- range(start, stop, step)
for _, x in f.range(5) do print(x) end
print('============')

for _, x in f.range(-5) do print(x) end
print('============')

for _, x in f.range(1, 6) do print(x) end
print('============')

for _it, v in f.range(0, 20, 5) do print(v) end
print('============')

for _it, v in f.range(0, 10, 3) do print(v) end
print('============')

for _it, v in f.range(0, 1.5, 0.2) do print(v) end
print('============')


-- duplicate

f.each(print, f.take(5, f.duplicate('a', 'b', 'c')))
print('============')

-- tabulate
f.each(f.print, f.take(5, f.tabulate(function(x)  return 'a', 'b', 2*x end)))
print('============')

f.each(print)
