package sofa.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sofa.auth.dal.UserMapper;
import sofa.auth.service.UserService;
import sofa.common.bean.dal.UserDO;
import sofa.common.bean.dto.BaseDTO;
import sofa.common.bean.dto.BaseErrorDTO;
import sofa.common.bean.dto.BaseSuccessDTO;
import sofa.common.bean.dto.TokenDTO;
import sofa.common.bean.model.BaseErrorEnum;
import sofa.common.bean.vo.UserVO;
import sofa.common.util.jwt.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseDTO login(UserVO userVO) throws Exception {
        QueryWrapper<UserDO> userDOQueryWrapper = new QueryWrapper<>();
        userDOQueryWrapper.eq("USER_ACCOUNT", userVO.getUserAccount());
        userDOQueryWrapper.eq("USER_PASSWORD", userVO.getUserPassword());
        UserDO userDO = userMapper.selectOne(userDOQueryWrapper);
        if (null == userDO) {
            return new BaseErrorDTO(BaseErrorEnum.USER_PASSWORD_WRONG);
        }
        BaseSuccessDTO baseSuccessDTO = new BaseSuccessDTO();
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(userDO.getUserId());
        String token = JwtUtil.encryptToken(tokenDTO);
        Map<String, String> result = new HashMap<>();
        result.put("appAuthToken", token);
        baseSuccessDTO.setData(result);
        return baseSuccessDTO;
    }
}
