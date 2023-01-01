package smu.poodle.smnavi.response;

import lombok.Getter;
import smu.poodle.smnavi.callapi.PathInfo;

import java.util.List;

@Getter
public class TransitResponse extends BaseResponse{

    private int pathInfoCnt;
    private List<PathInfo> pathInfoList;

    public TransitResponse(String resultCode, List<PathInfo> pathInfoList) {
        super(resultCode);
        this.pathInfoList = pathInfoList;
        this.pathInfoCnt = pathInfoList.size();
    }
}
