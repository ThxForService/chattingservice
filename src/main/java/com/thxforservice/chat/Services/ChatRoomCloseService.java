package com.thxforservice.chat.Services;

import com.thxforservice.chat.entities.ChatRoom;
import com.thxforservice.chat.exceptions.RoomNotFoundException;
import com.thxforservice.chat.repositories.ChatRoomRepository;
import com.thxforservice.global.exceptions.UnAuthorizedException;
import com.thxforservice.member.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomCloseService {

    private final MemberUtil memberUtil;
    private final ChatRoomRepository chatRoomRepository;


    /**
     * SOFT 삭제
     * @param roomNo
     * @return
     */
    public ChatRoom close(Long roomNo){

        ChatRoom room = chatRoomRepository.findById(roomNo).orElseThrow(RoomNotFoundException::new);
        String userEmail = room.getUserEmail();
        if(memberUtil.isAdmin() == false){
            if(userEmail != memberUtil.getMember().getEmail()) throw new UnAuthorizedException();
        }

        room.setDeletedAt(LocalDateTime.now());
        chatRoomRepository.saveAndFlush(room);

        return room;
    }
}
