package smu.poodle.smnavi.notice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.notice.domain.NoticeEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
//레포지토리 : DB와 관련된 로직을 담당하는 계층
//DB에 접근하여 데이터를 조회,추가,수정,삭제하는 기능을 제공
public interface NoticeRepository extends JpaRepository<NoticeEntity,Long> {
    //게시글 등록
    NoticeEntity save(NoticeEntity noticeEntity);
    //게시글 조회
    Optional<NoticeEntity> findById(Long id);
    List<NoticeEntity> findAll();
    List<NoticeEntity> findByTitleContainingOrContentContaining(String title, String content);

    //게시글 삭제
    //void delete(NoticeEntity noticeEntity);

    //기타
    int countByTitleAndContentAndRegDateIsGreaterThanEqual(String title, String content, LocalDateTime regDate);
//    List<NoticeEntity>search(String keyword);
}
