package com.bluecom.common.service.impl;

import org.springframework.stereotype.Repository;

import com.bluecom.common.domain.AlimTalkDTO;
import com.bluecom.common.domain.AlimTalkTemplateDTO;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class BgfAlimTalkDAO extends EgovAbstractMapper {

	public void insertAlimTalk(AlimTalkDTO alimTalk) {
		
		insert("bgfAlimTalkMapper.insertAlimTalk", alimTalk);
	}

	public AlimTalkTemplateDTO selectAlimTalkTemplate(AlimTalkTemplateDTO tempalte) {
		
		return selectOne("bgfAlimTalkMapper.selectAlimTalkTemplate", tempalte);
	}

}
