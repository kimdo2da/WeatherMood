//GET /contents/anime?page=1
//GET /contents/dramas?page=1
//GET /contents/anime/search?keyword=...&page=1
//GET /contents/dramas/search?keyword=...&page=1
// 애니 드라마 탭 검색창 검색결과 이전다음 콘텐츠 카드 출력 등이 있음!
import { motion } from "motion/react";
import { CozyCard } from "../components/CozyCard";
import type { CardContent, ContentTab } from "../types";

type ContentListScreenProps = {
  contentTab: ContentTab;
  contentItems: CardContent[];
  contentKeyword: string;
  setContentKeyword: (value: string) => void;
  isLoadingContents: boolean;
  contentError: string;
  contentPage: number;
  contentHasNext: boolean;
  loadContents: (tab: ContentTab, page?: number) => void | Promise<void>;
  searchContents: (page?: number) => void | Promise<void>;
};

export function ContentListScreen({
  contentTab,
  contentItems,
  contentKeyword,
  setContentKeyword,
  isLoadingContents,
  contentError,
  contentPage,
  contentHasNext,
  loadContents,
  searchContents,
}: ContentListScreenProps) {
  return (
    <div className="space-y-8">
      <div className="text-center space-y-2">
        <h2 className="text-4xl font-medium text-gray-800">콘텐츠 전체보기</h2>
        <p className="text-gray-600">
          애니와 드라마를 검색하고 감정에 맞는 작품을 찾아보세요.
        </p>
      </div>

      <div className="flex justify-center gap-3">
        <motion.button
          onClick={() => {
            setContentKeyword("");
            loadContents("anime", 1);
          }}
          className="px-6 py-3 font-medium"
          style={{
            background: contentTab === "anime" ? "#FFD93D" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow:
              contentTab === "anime"
                ? "4px 4px 0px #2D2D2D"
                : "2px 2px 0px #2D2D2D",
          }}
          whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
          whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
        >
          애니
        </motion.button>

        <motion.button
          onClick={() => {
            setContentKeyword("");
            loadContents("drama", 1);
          }}
          className="px-6 py-3 font-medium"
          style={{
            background: contentTab === "drama" ? "#A8E6CF" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow:
              contentTab === "drama"
                ? "4px 4px 0px #2D2D2D"
                : "2px 2px 0px #2D2D2D",
          }}
          whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
          whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
        >
          드라마
        </motion.button>
      </div>

      <div
        className="max-w-2xl mx-auto p-4 flex gap-3"
        style={{
          background: "#FFFFFF",
          borderRadius: "25px",
          border: "3px solid #2D2D2D",
          boxShadow: "4px 4px 0px #2D2D2D",
        }}
      >
        <input
          type="text"
          value={contentKeyword}
          onChange={(e) => setContentKeyword(e.target.value)}
          placeholder={
            contentTab === "anime"
              ? "검색할 애니 제목을 입력하세요"
              : "검색할 드라마 제목을 입력하세요"
          }
          className="flex-1 px-4 py-3 outline-none"
          style={{
            borderRadius: "18px",
            border: "2px solid #2D2D2D",
          }}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              searchContents(1);
            }
          }}
        />

        <motion.button
          onClick={() => searchContents(1)}
          className="px-6 py-3 font-medium"
          style={{
            background: "#FFD93D",
            color: "#2D2D2D",
            borderRadius: "18px",
            border: "2px solid #2D2D2D",
            boxShadow: "3px 3px 0px #2D2D2D",
          }}
          whileHover={{ y: -2, boxShadow: "4px 4px 0px #2D2D2D" }}
          whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
        >
          검색
        </motion.button>
      </div>

      {isLoadingContents && (
        <p className="text-center text-gray-700 font-medium">
          콘텐츠를 불러오는 중이에요...
        </p>
      )}

      {contentError && (
        <p className="text-center text-red-500 font-medium">{contentError}</p>
      )}

      {!isLoadingContents && contentItems.length === 0 && !contentError && (
        <p className="text-center text-gray-700 font-medium">
          표시할 콘텐츠가 없어요.
        </p>
      )}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
        {contentItems.map((content, index) => (
  <CozyCard
    key={`content-${contentTab}-${contentPage}-${content.type}-${content.title}-${index}`}
    {...content}
  />
))}
      </div>

      <div className="flex justify-center items-center gap-4 pt-4">
        <motion.button
          onClick={() => {
            const prevPage = contentPage - 1;

            if (contentKeyword.trim()) {
              searchContents(prevPage);
            } else {
              loadContents(contentTab, prevPage);
            }
          }}
          disabled={contentPage <= 1 || isLoadingContents}
          className="px-6 py-3 font-medium"
          style={{
            background: contentPage <= 1 ? "#E5E7EB" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow:
              contentPage <= 1
                ? "2px 2px 0px #2D2D2D"
                : "4px 4px 0px #2D2D2D",
            opacity: contentPage <= 1 ? 0.6 : 1,
          }}
          whileHover={
            contentPage <= 1
              ? {}
              : { y: -2, boxShadow: "5px 5px 0px #2D2D2D" }
          }
          whileTap={
            contentPage <= 1
              ? {}
              : { y: 1, boxShadow: "2px 2px 0px #2D2D2D" }
          }
        >
          ← 이전
        </motion.button>

        <span
          className="px-5 py-3 font-medium"
          style={{
            background: "#FFD93D",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow: "3px 3px 0px #2D2D2D",
          }}
        >
          {contentPage} 페이지
        </span>

        <motion.button
          onClick={() => {
            const nextPage = contentPage + 1;

            if (contentKeyword.trim()) {
              searchContents(nextPage);
            } else {
              loadContents(contentTab, nextPage);
            }
          }}
          disabled={!contentHasNext || isLoadingContents}
          className="px-6 py-3 font-medium"
          style={{
            background: !contentHasNext ? "#E5E7EB" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow: !contentHasNext
              ? "2px 2px 0px #2D2D2D"
              : "4px 4px 0px #2D2D2D",
            opacity: !contentHasNext ? 0.6 : 1,
          }}
          whileHover={
            !contentHasNext
              ? {}
              : { y: -2, boxShadow: "5px 5px 0px #2D2D2D" }
          }
          whileTap={
            !contentHasNext
              ? {}
              : { y: 1, boxShadow: "2px 2px 0px #2D2D2D" }
          }
        >
          다음 →
        </motion.button>
      </div>
    </div>
  );
}