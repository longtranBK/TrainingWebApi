package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

	@Query(value = "SELECT"
			+ " c.id AS commentId,"
			+ " ui.full_name AS fullName,"
			+ " c.content,"
			+ " (SELECT count(1) FROM comment_likes cl inner join comments cs on cs.id = cl.comment_id"
			+ "	WHERE cs.id = c.id and cs.del_flg = false) as countLikes,"
			+ " c.create_ts AS createTs,"
			+ " c.update_ts AS updateTs"
			+ " FROM comments c "
			+ " INNER JOIN user_infor ui on c.user_id = ui.id"
			+ " WHERE c.post_id = ?1 AND c.del_flg = false ORDER BY createTs DESC"
			+ " LIMIT ?3,?2", nativeQuery = true)
	List<CommentCustomResDto> findByPostIdCustom(String postId, int limit, int offset);
	
	@Modifying
	@Query("update comments c set c.delFlg = ?2 where c.postId= ?1")
	void updateDelFlg(String postId, boolean delFlg);
	
	Comment findByCommentIdAndUserIdAndDelFlg(String commentId, String userId, boolean delFlg);
	
	@Query(value = "SELECT count(user_id) FROM comments"
			+ " WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	int countCommentWithTime(String userId, Date startDate, Date endDate);
}
