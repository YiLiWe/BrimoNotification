package com.example.brimonotification.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.brimonotification.room.entity.BillEntity;

import java.util.List;

@Dao
public interface BillDao {
    @Insert
    long insert(BillEntity bill);

    @Query("SELECT * FROM bill LIMIT :limit OFFSET :offset")
    List<BillEntity> queryPageVideo(int limit, int offset);

    @Query("SELECT * FROM bill where state=:state LIMIT :limit OFFSET :offset")
    List<BillEntity> queryByState(int limit, int offset, int state);

    //根据单词名称修改单词等级
    @Query("UPDATE bill SET state= :state WHERE uid = :id")
    void updateStateById(long id, int state);

    @Query("DELETE FROM bill")
    void deleteAll();
}
