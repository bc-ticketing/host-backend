package com.idetix.hostbackend.Controller;

import com.idetix.hostbackend.Messages.Greeting;
import com.idetix.hostbackend.Messages.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendToUser("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception{
        Thread.sleep(1000);
        return new Greeting("Hello, "+ message.getName() + "!");
    }

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception{
//        Thread.sleep(1000);
//        return new Greeting("Hello, "+ message.getName() + "!");
//    }


}
