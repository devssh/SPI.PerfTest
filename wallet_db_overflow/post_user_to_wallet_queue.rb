require "rubygems"
require "bunny"

#connection = Bunny.new("amqp://admin:admin@192.168.57.103:5672")
connection = Bunny.new("amqp://guest:guest@localhost:5672")
connection.start
channel  = connection.create_channel
(0..39).each do |id|
  4.times do 
    channel.default_exchange.publish('{"id":"d'+ format('%02d', id) +'"}', :routing_key => "user_verify")
  end
end

