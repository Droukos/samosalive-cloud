package com.droukos.aedservice.repo;

import com.droukos.aedservice.environment.dto.server.aed.aedEvent.EventPreviewUsersDto;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.UnionEventDeviceRescuerDto;
import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AedEventRepository extends ReactiveMongoRepository<AedEvent, String> {
    Flux<AedEvent> findAedEventsByStatus(Integer status);
    Flux<AedEvent> findAedEventsByOccurrenceType(Integer occurrenceType);
    Flux<AedEvent> findAedEventsByOccurrenceTypeAndStatus(Integer occurrenceType, Integer status);
    Flux<AedEvent> findAllByRescuerLike(String rescuer);

    @Aggregation({
            "{$match: { '_id': new ObjectId('?0') }}",
            "{$addFields: { 'root.aedEvent': '$$ROOT' }}",
            "{$replaceRoot: { 'newRoot': '$root' }}",
            "{$unionWith: { coll: 'aedDevice', " +
                    "pipeline: [" +
                    "{$match: { '_id': new ObjectId('?1') }}, " +
                    "{$addFields: { 'root.aedDevice': '$$ROOT' }}," +
                    "{$replaceRoot: { newRoot: '$root' }}]}}",
            "{$unionWith: { coll: 'userRes', " +
                    "pipeline: [" +
                    "{$match: { 'user': '?2' }}, " +
                    "{$addFields: { 'root.userRes': '$$ROOT' }}," +
                    "{$replaceRoot: { newRoot: '$root' }}]}}",
            "{$group: { '_id': 0, "+
                    "userRes: {$mergeObjects: '$userRes'},"+
                    "aedDevice: {$mergeObjects: '$aedDevice'},"+
                    "aedEvent: {$mergeObjects: '$aedEvent'}}}"
    })
    Mono<UnionEventDeviceRescuerDto> getUnionEventDeviceRescuer(String aedEventId, String aedDeviceId, String user);

    @Aggregation({
            "{$match: { '_id': new ObjectId('?0') }}",
            "{$lookup: { from: 'userRes'," +
                    "let: { subs: '$subs'}," +
                    "pipeline: [" +
                    "{$match: {$expr: {$in: ['$user', '$$subs']}}}," +
                    "{$group: { _id: '$_id', 'username': {$first: '$user'}, avatar: {$first: '$prsn.prof.av'}, status: {$first: '$appState.status'}, roles: {$first: '$allRoles.role'} }}"+
                    "]," +
                    "as: 'subs.users' }}",
            "{$addFields: {'subs.username': '$username'}}",
            "{$replaceRoot: {newRoot: '$subs'}}"

    })
    Mono<EventPreviewUsersDto> getEventPreviewUsers(String aedEventId);
}
