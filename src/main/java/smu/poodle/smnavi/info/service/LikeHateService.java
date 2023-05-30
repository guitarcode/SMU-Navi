package smu.poodle.smnavi.info.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.info.domain.InfoEntity;
import smu.poodle.smnavi.info.domain.LikeHateEntity;
import smu.poodle.smnavi.info.repository.InfoRepository;
import smu.poodle.smnavi.info.repository.LikeHateRepository;
import smu.poodle.smnavi.user.domain.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeHateService {
    private final LikeHateRepository likeHateRepository;
    private final InfoRepository infoRepository;

    public int checkLikeOrHate(UserEntity user, long boardId, int identify) {
        Optional<LikeHateEntity> likeOrHate = likeHateRepository.findByUserAndBoard_Id(user, boardId);
        //좋아요->좋아요 : 좋아요 취소
        //좋아요->싫어요 : 좋아요 취소 싫어요 증가
        //null->좋아요 : 좋아요 증가
        //null->싫어요 : 싫어요 증가
        //싫어요->좋아요 : 좋아요 증가 싫어요 감소
        //싫어요->싫어요 : 싫어요 취소
        if (likeOrHate.isPresent()) { //누른 전적이 있음. 수정 삭제
            //삭제
            if (identify == likeOrHate.get().getIdentify()) {
                likeHateRepository.delete(likeOrHate.get());
            } else { //수정
                likeOrHate.get().setIdentify(identify);
                likeHateRepository.save(likeOrHate.get());
                return likeOrHate.get().getIdentify();
            }
        } else { //누른 전적이 없음
            Optional<InfoEntity> board = infoRepository.findById(boardId);
            LikeHateEntity likeHateEntity = new LikeHateEntity();
            likeHateEntity.setUser(user);
            likeHateEntity.setBoard(board.get());
            likeHateEntity.setIdentify(identify);
            likeHateRepository.save(likeHateEntity);
            return likeHateEntity.getIdentify();
        }
        return -1; //-1은 그냥 없는거야! 0은 싫어요, 1은 좋아요야.
    }

    public Object countByBoard_IdAndIdentify(UserEntity user, Long boardId) {
        Map<String, Object> result = new HashMap<>();
        int like = likeHateRepository.countByBoard_IdAndIdentify(boardId, 1); //좋아요 개수
        int hate = likeHateRepository.countByBoard_IdAndIdentify(boardId, 0); //싫어요개수
        Optional<LikeHateEntity> likeOrHate = likeHateRepository.findByUserAndBoard_Id(user, boardId); //눌렀는지 아닌지 여부
        result.put("like", like);
        result.put("hate", hate);

        if (likeOrHate.isPresent()) {
            result.put("emotion", likeOrHate.get().getIdentify());
        } else {
            result.put("emotion", -1); //아무런 공감도 표시하지 않음
        }
        return result;
    }
}
