local flag = redis.call('EXISTS', KEYS[1])
if flag == 0
    then
        redis.call('INCRBY',KEYS[1],ARGV[1]-1)
        redis.call('EXPIRE',KEYS[1],ARGV[2])
        return true
    end
local number =redis.call('DECR', KEYS[1])
if number >= 0
    then return true
    end
return false