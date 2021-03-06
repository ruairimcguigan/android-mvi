package com.consistence.pinyin.app.study

import com.consistence.pinyin.domain.study.Study
import com.memtrip.mxandroid.MxViewIntent

sealed class StudyIntent : MxViewIntent {
    object Init : StudyIntent()
    object Idle : StudyIntent()
    object Refresh : StudyIntent()
    object Retry : StudyIntent()
    data class TrainPhrase(val study: Study) : StudyIntent()
    data class SelectStudy(val study: Study) : StudyIntent()
}