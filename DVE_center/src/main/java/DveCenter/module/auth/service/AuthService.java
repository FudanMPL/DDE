package DveCenter.module.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import DveAgent.common.R;
import DveAgent.entity.Agent;
import DveAgent.entity.Center;
import DveAgent.mapper.AgentMapper;
import reactor.core.publisher.Flux;

@Service
public class AuthService {
    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private CenterWebClientService centerWebClientService;

    public void broadcast(Center me) throws Exception {
        LambdaQueryWrapper<Agent> agentListQuery = Wrappers.<Agent>lambdaQuery();
        List<Agent> agents = agentMapper.selectList(agentListQuery);
        Flux.fromIterable(agents).flatMap((Agent a) -> {
            try {
                return centerWebClientService.center2AgentWebClient(a.getUid()).post().uri("/centers/update")
                        .bodyValue(me).retrieve().bodyToMono(R.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collectList().block();
    }
}
