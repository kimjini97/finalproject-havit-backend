package com.havit.finalbe.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {

    // 동시성을 고려하여 ConcurrentHashMap 사용  -> 가능한 많은 클라이언트의 요청을 처리할 수 있도록 하는 것
    public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // Emitter 를 저장
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // 이벤트 저장
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    // 구분자를 회원 ID 를 사용하기에 StartWith 를 사용 - 회원과 관련된 모든 Emitter 를 찾는다.
    public Map<String, SseEmitter> findAllEmittersStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream() // key / value entry 리턴
                .filter(entry -> entry.getKey().startsWith(memberId))
                // 해당 userId 로 시작하는 키값을 필터 key, value 리턴
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 회원에게 수신된 모든 이벤트를 찾는다.
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // pk 를 통해 Emitter 를 제거
    public void deleteByEmitterId(String emitterId) {
        emitters.remove(emitterId);
    }

    // 회원 ID 와 관련된 Emitter 를 모두 제거
    public void deleteAllEmittersStartWithMemberId(String memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    // 회원 ID 와 관련된 모든 이벤트 캐시를 삭제
    public void deleteAllEventCacheStartWithMemberId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
