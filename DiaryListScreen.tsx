//GET /diaries
//GET /diaries/{diaryId}
//PUT /diaries/{diaryId}
//DELETE /diaries/{diaryId}
//일기 목록출력 상세보기 수정 삭제가있다
import { motion } from "motion/react";
import type { DiaryDetailData, DiaryItem, DiaryMode } from "../types";

type DiaryListScreenProps = {
  diaryMode: DiaryMode;
  diaries: DiaryItem[];
  selectedDiary: DiaryDetailData | null;

  isLoadingDiaries: boolean;
  isLoadingDiaryDetail: boolean;
  isUpdatingDiary: boolean;
  isDeletingDiary: boolean;

  diaryError: string;

  diaryTitle: string;
  setDiaryTitle: (value: string) => void;

  diaryMoodText: string;
  setDiaryMoodText: (value: string) => void;

  diaryContent: string;
  setDiaryContent: (value: string) => void;

  setDiaryMode: (mode: DiaryMode) => void;
  setSelectedDiary: (diary: DiaryDetailData | null) => void;

  loadDiaryDetail: (diaryId: number) => void | Promise<void>;
  openDiaryEdit: () => void;
  handleUpdateDiary: () => void | Promise<void>;
  handleDeleteDiary: () => void | Promise<void>;
};

export function DiaryListScreen({
  diaryMode,
  diaries,
  selectedDiary,
  isLoadingDiaries,
  isLoadingDiaryDetail,
  isUpdatingDiary,
  isDeletingDiary,
  diaryError,
  diaryTitle,
  setDiaryTitle,
  diaryMoodText,
  setDiaryMoodText,
  diaryContent,
  setDiaryContent,
  setDiaryMode,
  setSelectedDiary,
  loadDiaryDetail,
  openDiaryEdit,
  handleUpdateDiary,
  handleDeleteDiary,
}: DiaryListScreenProps) {
  return (
    <div className="space-y-8">
      {diaryMode === "list" && (
        <>
          <div className="text-center space-y-2">
            <h2 className="text-4xl font-medium text-gray-800">
              내 감성 일기장
            </h2>
            <p className="text-gray-600">
              감성 테스트 결과와 함께 남긴 일기를 확인해보세요.
            </p>
          </div>

          {isLoadingDiaries && (
            <p className="text-center text-gray-700 font-medium">
              일기 목록을 불러오는 중이에요...
            </p>
          )}

          {diaryError && (
            <p className="text-center text-red-500 font-medium">
              {diaryError}
            </p>
          )}

          {!isLoadingDiaries && diaries.length === 0 && !diaryError && (
            <p className="text-center text-gray-700 font-medium">
              아직 작성한 일기가 없어요.
            </p>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {diaries.map((diary) => (
              <motion.div
                key={diary.diaryId}
                onClick={() => loadDiaryDetail(diary.diaryId)}
                className="p-6 text-left cursor-pointer"
                style={{
                  background: "#FFFFFF",
                  borderRadius: "25px",
                  border: "3px solid #2D2D2D",
                  boxShadow: "4px 4px 0px #2D2D2D",
                }}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                whileHover={{
                  y: -4,
                  boxShadow: "6px 6px 0px #2D2D2D",
                }}
                whileTap={{
                  y: 1,
                  boxShadow: "2px 2px 0px #2D2D2D",
                }}
              >
                <div className="space-y-3">
                  <div className="flex items-center justify-between gap-4">
                    <h3 className="text-xl font-medium text-gray-800">
                      {diary.title}
                    </h3>
                    <span className="text-sm text-gray-500">
                      #{diary.diaryId}
                    </span>
                  </div>

                  <div className="space-y-1 text-gray-700">
                    <p>감정: {diary.moodText}</p>
                    <p>루트: {diary.routeName}</p>
                    <p>엔딩: {diary.endingName}</p>
                  </div>

                  <p className="text-sm text-gray-500">
                    {diary.createdAt.replace("T", " ")}
                  </p>

                  <p
                    className="text-sm font-medium"
                    style={{ color: "#FF6B6B" }}
                  >
                    자세히 보기 →
                  </p>
                </div>
              </motion.div>
            ))}
          </div>
        </>
      )}

      {diaryMode === "detail" && (
        <>
          <motion.button
            onClick={() => {
              setDiaryMode("list");
              setSelectedDiary(null);
            }}
            className="px-5 py-3 font-medium"
            style={{
              background: "#FFFFFF",
              color: "#2D2D2D",
              borderRadius: "25px",
              border: "3px solid #2D2D2D",
              boxShadow: "3px 3px 0px #2D2D2D",
            }}
            whileHover={{ y: -2, boxShadow: "4px 4px 0px #2D2D2D" }}
            whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
          >
            ← 일기 목록
          </motion.button>

          {isLoadingDiaryDetail && (
            <p className="text-center text-gray-700 font-medium">
              일기 상세를 불러오는 중이에요...
            </p>
          )}

          {diaryError && (
            <p className="text-center text-red-500 font-medium">
              {diaryError}
            </p>
          )}

          {selectedDiary && (
            <motion.div
              className="p-8 space-y-6"
              style={{
                background: "#FFFFFF",
                borderRadius: "30px",
                border: "4px solid #2D2D2D",
                boxShadow: "6px 6px 0px #2D2D2D",
              }}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
            >
              <div className="space-y-2">
                <h2 className="text-4xl font-medium text-gray-800">
                  {selectedDiary.title}
                </h2>
                <p className="text-gray-600">
                  감정: {selectedDiary.moodText}
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-4 gap-4 text-gray-700">
                <div>날씨: {selectedDiary.resultSummary.weatherName}</div>
                <div>루트: {selectedDiary.resultSummary.routeName}</div>
                <div>엔딩: {selectedDiary.resultSummary.endingName}</div>
                <div>대표 감정: {selectedDiary.resultSummary.mainEmotion}</div>
              </div>

              <div
                className="p-5 text-gray-800 whitespace-pre-wrap"
                style={{
                  background: "#FFF7D6",
                  borderRadius: "20px",
                  border: "3px solid #2D2D2D",
                }}
              >
                {selectedDiary.content}
              </div>

              <div className="text-sm text-gray-500 space-y-1">
                <p>작성일: {selectedDiary.createdAt.replace("T", " ")}</p>
                <p>수정일: {selectedDiary.updatedAt.replace("T", " ")}</p>
              </div>

              <div className="flex gap-3">
                <motion.button
                  onClick={openDiaryEdit}
                  className="px-6 py-3 font-medium"
                  style={{
                    background: "#FFD93D",
                    color: "#2D2D2D",
                    borderRadius: "25px",
                    border: "3px solid #2D2D2D",
                    boxShadow: "4px 4px 0px #2D2D2D",
                  }}
                  whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
                  whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
                >
                  수정
                </motion.button>

                <motion.button
                  onClick={handleDeleteDiary}
                  disabled={isDeletingDiary}
                  className="px-6 py-3 font-medium"
                  style={{
                    background: "#FF6B6B",
                    color: "#2D2D2D",
                    borderRadius: "25px",
                    border: "3px solid #2D2D2D",
                    boxShadow: "4px 4px 0px #2D2D2D",
                    opacity: isDeletingDiary ? 0.6 : 1,
                  }}
                  whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
                  whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
                >
                  {isDeletingDiary ? "삭제 중..." : "삭제"}
                </motion.button>
              </div>
            </motion.div>
          )}
        </>
      )}

      {diaryMode === "edit" && selectedDiary && (
        <div className="min-h-[60vh] flex items-center justify-center">
          <motion.div
            className="w-full max-w-2xl p-8 space-y-6"
            style={{
              background: "#FFFFFF",
              borderRadius: "30px",
              border: "4px solid #2D2D2D",
              boxShadow: "6px 6px 0px #2D2D2D",
            }}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <div className="text-center space-y-2">
              <h2 className="text-3xl font-medium text-gray-800">
                감성 일기 수정
              </h2>
              <p className="text-gray-600 text-sm">
                기록한 감정과 내용을 다시 정리해보세요.
              </p>
            </div>

            <div className="space-y-4">
              <input
                type="text"
                value={diaryTitle}
                onChange={(e) => setDiaryTitle(e.target.value)}
                placeholder="일기 제목"
                className="w-full px-4 py-3 outline-none"
                style={{
                  borderRadius: "18px",
                  border: "3px solid #2D2D2D",
                }}
              />

              <input
                type="text"
                value={diaryMoodText}
                onChange={(e) => setDiaryMoodText(e.target.value)}
                placeholder="오늘의 감정"
                className="w-full px-4 py-3 outline-none"
                style={{
                  borderRadius: "18px",
                  border: "3px solid #2D2D2D",
                }}
              />

              <textarea
                value={diaryContent}
                onChange={(e) => setDiaryContent(e.target.value)}
                placeholder="오늘의 감정과 작품 추천에 대한 생각을 적어보세요."
                className="w-full px-4 py-3 outline-none min-h-48 resize-none"
                style={{
                  borderRadius: "18px",
                  border: "3px solid #2D2D2D",
                }}
              />

              {diaryError && (
                <p className="text-sm text-red-500 font-medium">
                  {diaryError}
                </p>
              )}

              <div className="flex gap-3">
                <motion.button
                  onClick={() => setDiaryMode("detail")}
                  className="w-full py-4 font-medium"
                  style={{
                    background: "#FFFFFF",
                    color: "#2D2D2D",
                    borderRadius: "25px",
                    border: "3px solid #2D2D2D",
                    boxShadow: "4px 4px 0px #2D2D2D",
                  }}
                  whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
                  whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
                >
                  취소
                </motion.button>

                <motion.button
                  onClick={handleUpdateDiary}
                  disabled={isUpdatingDiary}
                  className="w-full py-4 font-medium"
                  style={{
                    background: "#FFD93D",
                    color: "#2D2D2D",
                    borderRadius: "25px",
                    border: "3px solid #2D2D2D",
                    boxShadow: "4px 4px 0px #2D2D2D",
                    opacity: isUpdatingDiary ? 0.6 : 1,
                  }}
                  whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
                  whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
                >
                  {isUpdatingDiary ? "수정 중..." : "수정 완료"}
                </motion.button>
              </div>
            </div>
          </motion.div>
        </div>
      )}
    </div>
  );
}