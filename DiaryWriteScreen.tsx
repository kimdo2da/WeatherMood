//post /diaries
//결과 resultId와 연결된 일기 작성, 제목 감정 내용 저장이 있음
import { motion } from "motion/react";

type DiaryWriteScreenProps = {
  diaryTitle: string;
  setDiaryTitle: (value: string) => void;
  diaryMoodText: string;
  setDiaryMoodText: (value: string) => void;
  diaryContent: string;
  setDiaryContent: (value: string) => void;
  diaryError: string;
  isCreatingDiary: boolean;
  handleCreateDiary: () => void | Promise<void>;
  onCancel: () => void;
};

export function DiaryWriteScreen({
  diaryTitle,
  setDiaryTitle,
  diaryMoodText,
  setDiaryMoodText,
  diaryContent,
  setDiaryContent,
  diaryError,
  isCreatingDiary,
  handleCreateDiary,
  onCancel,
}: DiaryWriteScreenProps) {
  return (
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
            감성 일기 작성
          </h2>
          <p className="text-gray-600 text-sm">
            선택한 감성 결과를 바탕으로 오늘의 기록을 남겨보세요.
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
            <p className="text-sm text-red-500 font-medium">{diaryError}</p>
          )}

          <div className="flex gap-3">
            <motion.button
              onClick={onCancel}
              className="w-full py-4 font-medium"
              style={{
                background: "#FFFFFF",
                color: "#2D2D2D",
                borderRadius: "25px",
                border: "3px solid #2D2D2D",
                boxShadow: "4px 4px 0px #2D2D2D",
              }}
              whileHover={{
                y: -2,
                boxShadow: "5px 5px 0px #2D2D2D",
              }}
              whileTap={{
                y: 1,
                boxShadow: "2px 2px 0px #2D2D2D",
              }}
            >
              취소
            </motion.button>

            <motion.button
              onClick={handleCreateDiary}
              disabled={isCreatingDiary}
              className="w-full py-4 font-medium"
              style={{
                background: "#FFD93D",
                color: "#2D2D2D",
                borderRadius: "25px",
                border: "3px solid #2D2D2D",
                boxShadow: "4px 4px 0px #2D2D2D",
                opacity: isCreatingDiary ? 0.6 : 1,
              }}
              whileHover={{
                y: -2,
                boxShadow: "5px 5px 0px #2D2D2D",
              }}
              whileTap={{
                y: 1,
                boxShadow: "2px 2px 0px #2D2D2D",
              }}
            >
              {isCreatingDiary ? "작성 중..." : "일기 저장"}
            </motion.button>
          </div>
        </div>
      </motion.div>
    </div>
  );
}