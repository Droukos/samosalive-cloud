package com.droukos.aedservice.controller.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.*;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedAedProblems;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.model.factories.aed_problems.AedProblemsFactoryClose;
import com.droukos.aedservice.model.factories.aed_problems.AedDeviceProblemFactoryCreate;
import com.droukos.aedservice.model.factories.aed_problems.AedProblemsFactorySubTechnical;
import com.droukos.aedservice.service.aed_problem.AedProblemsChannel;
import com.droukos.aedservice.service.aed_problem.AedProblemsCreation;
import com.droukos.aedservice.service.aed_problem.AedProblemsInfo;
import com.droukos.aedservice.service.aed_problem.AedProblemsServices;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedProblemsController {

    private final AedProblemsCreation aedProblemsCreation;
    private final AedProblemsServices aedProblemsServices;
    private final AedProblemsChannel aedProblemsChannel;
    private final AedProblemsInfo aedProblemsInfo;

    @MessageMapping("aed.problems.post")
    public Mono<Boolean> postAedDeviceProblem(AedDeviceProblemDtoCreate dtoCreate){
        return Mono.just(dtoCreate)
                .doOnNext(aedProblemsCreation::validateProblems)
                .zipWith(aedProblemsCreation.getDeviceById(dtoCreate))
                .flatMap(AedDeviceProblemFactoryCreate::aedDeviceBrokenProblemMono)
                .flatMap(aedProblemsServices::saveAedDevice)
                .flatMap(aedProblemsCreation::saveAedProblem)
                .flatMap(aedProblemsCreation::publishProblemOnRedisChannel)
                .then(Mono.just(true));
    }

    @MessageMapping("aed.problems.get")
    public Flux<RequestedPreviewAedProblems> findProblems(AedProblemsDtoSearch aedProblemsDtoSearch) {
        return Flux.just(aedProblemsDtoSearch)
                .doOnNext(aedProblemsInfo::validateTitle)
                .flatMap(aedProblemsInfo::findProblemsByTitle)
                .flatMap(RequestedPreviewAedProblems::buildMono);
    }

    @MessageMapping("aed.problems.getId")
    public Mono<RequestedAedProblems> findProblemsId(AedProblemsDtoIdSearch aedProblemsDtoIdSearch){
        return Mono.just(aedProblemsDtoIdSearch.getId())
                .flatMap(aedProblemsInfo::findProblemsId)
                .flatMap(RequestedAedProblems::buildMono);
    }

    @MessageMapping("aed.problems.listen")
    public Flux<RequestedPreviewAedProblems> listenProblems() {
        return aedProblemsInfo
                .fetchPublishedAedProblems()
                .flatMap(RequestedPreviewAedProblems::buildMono);
    }

    @MessageMapping("aed.problems.subTechnical")
    public Mono<AedProblems> setProblemsTechnical(AedProblemsDtoTechnicalSub aedProblemsDtoTechnicalSub){
        return Mono.just(aedProblemsDtoTechnicalSub.getId())
                .flatMap(aedProblemsInfo::findProblemsId)
                .zipWith(Mono.just(aedProblemsDtoTechnicalSub))
                .flatMap(AedProblemsFactorySubTechnical::subTechnicalMono)
                .flatMap(aedProblemsInfo::saveAedProblems)
                .flatMap(aedProblemsChannel::publishProblemOnRedisChannel);
                //.flatMap(aedProblemsChannel::publishEventOnRedisSingleChannel);
    }

    @MessageMapping("aed.problems.close")
    public Mono<Boolean> closeAedProblems(AedProblemsDtoClose aedProblemsDtoClose){
        return aedProblemsInfo.findProblemsId(aedProblemsDtoClose.getId())
                .zipWith(Mono.just(aedProblemsDtoClose))
                .flatMap(aedProblemsInfo::fetchDeviceFromProblemModel)
                .flatMap(AedProblemsFactoryClose::closeAedDeviceProblem)
                .flatMap(aedProblemsServices::saveAedDevice)
                .flatMap(aedProblemsInfo::saveAedProblems)
                //.flatMap(aedProblemsChannel::publishEventOnRedisSingleChannel)
                .then(Mono.just(true));
    }
}
