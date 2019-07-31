package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
//这里的这个mapper，本质上来说，就是说我接下来的的这个interface是要和smyql里面的表一一匹配的。
@Mapper
public interface UserDAO {
    //这里为了方便，可以先把我们估计注定要用的一些写sql语句用的string先记录成field变量，这样可以节约打字时间。
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url";


    //这个在我看来属实是一种怪异的写法。这个写法的意思应该就是运转了上面的@Insert后面的脚本，
    // 如果外人想要召唤这个脚本，开始使用他，那么就需要呼唤他作为一个method的名字，那就是addUser
    //脚本里面的这些value的名字，我姑且认为都是干脆直接等同于object的定义中的field的名字。
    //以及这里要注意一件事，那就是变量注定是被#{}给括起来的。
    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    //接下来就又是很自然的Select，各种Select。
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
