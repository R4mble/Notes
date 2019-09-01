local _ = require 'fun'

function love.load()
    x, y, w, h = 20, 20, 60, 20
end

function love.update()
    down = love.mouse.isDown(1)
    if down then
        local a, b = love.mouse.getPosition()
    end
end



function love.draw()
    love.graphics.setColor(0, 0.4, 0.4)
    love.graphics.rectangle("fill", x, y, w, h)
end
