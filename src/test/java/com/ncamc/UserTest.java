package com.ncamc;

import com.ncamc.config.JwtProperties;
import com.ncamc.entity.User;
import com.ncamc.mapper.UserMapper;
import com.ncamc.utils.CollectionUtils;
import com.ncamc.utils.JwtUtils;
import io.lettuce.core.cluster.SlotHash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:09
 */
@SpringBootTest
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void test1(){
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void test2() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjIiLCJleHAiOjE2NTc1MDU5ODB9.DCOGm20xaRH6xhBktfDAI22kARiD5SI1QB0uVvcITISubVhkVax58OqQToUfyco7YHkgGBs53IXaw9Gt6AMbA-NcPXT0apUdIyzoIqHiywEN5ypqJo-skv-evYwrrIrIdvcRoOg1HSDdaJisHuc1OWNoNXZMTy58FLvVLO4Mm5E";
        Long id = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        System.out.println(id);
    }

    @Test
    public void test3(){
        System.out.println(SlotHash.getSlot("A"));
        System.out.println(SlotHash.getSlot("B"));
        System.out.println(SlotHash.getSlot("C"));
        System.out.println(SlotHash.getSlot("hello"));
    }

    @Test
    public void test4(){
        List<String> 当前部门 = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        list1.add("4");
        list1.add("9");
        List<String> 需要修改的部门 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.add("3");
        list2.add("4");
        list2.add("5");
        list2.add("6");

//        System.out.println(getDiffrent(list1, list2));

//        System.out.println(getDiffByHashMap(list1, list2));

        Collection diff1 = CollectionUtils.diff(list1, list2);// list1中有，list2中没有的元素
        System.out.println(diff1.toString());
        Collection diff2 = CollectionUtils.diff(list2, list1);// list1中没有，list2中有的元素
        System.out.println(diff2.toString());
//
//        Collection exists=new ArrayList(list2);
//        Collection notexists=new ArrayList(list2);
//
//        exists.removeAll(list1);
//        System.out.println("list2中不存在于list1中的："+exists);
//        notexists.removeAll(exists);
//        System.out.println("list2中存在于list1中的："+notexists);
    }

    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        long st = System.nanoTime();
        List<String> diff = new ArrayList<String>();
        List<String> dif = new ArrayList<String>();
        List<String> l1=new ArrayList<>();
        List<String> l2=new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            l1.add(list1.get(i)+"");
        }
        for (int i = 0; i < list2.size(); i++) {
            l2.add(list2.get(i)+"");
        }
        List<String> maxList = l1;
        List<String> minList = l2;
        if(list2.size()>list1.size())
        {
            maxList = l2;
            minList = l1;
        }
        Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            if(map.get(string)!=null)
            {
                map.put(string, 2);
                continue;
            }
            diff.add(string);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        for (int i = 0; i < diff.size(); i++) {
            BigDecimal bd = new BigDecimal(diff.get(i));
            dif.add(String.valueOf(Integer.valueOf(bd.toPlainString())));
        }
        return dif;
    }

    public static List<String> getDiffByHashMap(List<String> list1,List<String> list2){
        long start = System.currentTimeMillis();
        List<String> resultList = new ArrayList<String>();
        //为了防止map扩容增加性能代价；初始化长度为两个链表的长度
        int capacity = (int)((float)(list1.size()+list2.size())/0.75F+1.0F);
        Map<String,Integer> map = new HashMap<>(capacity);
        for (String s:list1) {
            map.put(s,1);
        }
        for (String s:list2) {
            if(map.get(s) == null){
                map.put(s,1);
            }else {
                map.put(s,map.get(s)+1);
            }
        }
        for (Map.Entry m :map.entrySet()) {
            //value 大于1说明元素是两个list共有
            if((int)m.getValue() == 1){
                resultList.add((String)m.getKey());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("getDiffByHashMap耗时："+(end-start));
        return resultList;
    }

}