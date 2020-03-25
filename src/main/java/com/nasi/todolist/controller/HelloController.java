package com.nasi.todolist.controller;

import com.nasi.todolist.mapper.userMapper;
import com.nasi.todolist.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.sql.*;
import java.util.List;
import java.util.UUID;

import com.nasi.todolist.mapper.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.nasi.todolist.util.Base.MYSUCCESS;


@Controller
@EnableAutoConfiguration

public class HelloController {

    @Autowired
    private userMapper usermapper;

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }
   /* @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
    /*@RequestMapping("/login")
    public String getUser(Model model,
                        @RequestParam(name = "ID",defaultValue = "") String ID,
                        @RequestParam(name = "PASSWD",defaultValue = "") String PASSWD
    )
    {
        System.out.println("[I]  inter control"+ID+PASSWD);

        User newnuser=usermapper.selectUser(ID,getSha1(PASSWD));

        if(newnuser.PASSWD.equals(PASSWD)){
            System.out.println("[I]  out control");
            return "hello";
        }else{
            System.out.println("[I]  out control");
            return "index";
        }
    }*/
    @GetMapping(value = "/hello")
    public String Hello(Model model,
                        @RequestParam(name = "ID",defaultValue = "") String ID,
                        @RequestParam(name = "PASSWD",defaultValue = "") String PASSWD,
                        @RequestParam(name = "EMAIL",defaultValue = "") String EMAIL
                        )
    {

        model.addAttribute("name", "world");
        System.out.println("[I]  inter control"+ID+PASSWD+EMAIL);

        UUID uuid = UUID.randomUUID();
        User newnuser=new User();
        newnuser.EMAIL=EMAIL;
        newnuser.ID=ID;
        newnuser.UUID=uuid.toString();
        newnuser.PASSWD=getSha1(PASSWD);

        usermapper.addUser(newnuser);
        System.out.println("[I]  out control");
        return "hello";
    }

    public int addUser(User user)
    {
        String updates="insert into User(UUID,ID,EMAIL,PASSWD) values(?,?,?,?)";
        int resRow = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(updates,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,user.UUID);
                ps.setString(2,user.ID);
                ps.setString(3,user.EMAIL);
                ps.setString(4,getSha1(user.PASSWD));
                return ps;
            }
        });
        System.out.println("操作记录数："+resRow);

        return MYSUCCESS;
    }



    @Resource
    private JdbcTemplate jdbcTemplate;


        public String getUserList(){

        String sols="select * from User ";

        List<User> userList = jdbcTemplate.query(sols, new RowMapper<User>() {
            User user = null;
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                user =new User();
                user.UUID=resultSet.getString("UUID");
                user.PASSWD=resultSet.getString("PASSWD");
                user.ID=resultSet.getString("ID");
                user.EMAIL=resultSet.getString("EMAIL");
                return user;
            }});

        for(User user:userList){
            System.out.println(getSha1(user.ID));
            System.out.println(user.PASSWD);
            System.out.println(user.EMAIL);
            System.out.println(user.UUID);
        }
        //map.addAttribute("Items",userList);
        return "Hello todoList.";
    }
    public static String getSha1(String str) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
