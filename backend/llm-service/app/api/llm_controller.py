import logging

import openai
from fastapi import APIRouter
from fastapi.responses import StreamingResponse

from app.message.reuqest_llm_message import RequestLLMMessage

logger = logging.getLogger(__name__)
router = APIRouter()


async def generate_sse_stream(response):
    """
    Langchain의 스트리밍 응답을 SSE 방식으로 처리하는 함수
    """
    # LLM의 스트리밍 출력을 받아들이기 위한 함수
    collected_message = ""
    for chunk in response:
        # SSE 포맷으로 반환
        chunk_message = chunk.choices[0].delta.content or ""
        if chunk_message:
            collected_message += chunk_message
            if len(collected_message) > 10:
                yield f"data: {collected_message}\n\n"
                collected_message = ""

    logger.info("LLM response streaming completed.")
    # 마지막 메시지 반환
    if collected_message:
        yield f"data: {collected_message}\n\n"


@router.post("/upgrade-sentence")
async def upgrade_sentence_llm(request_llm_message: RequestLLMMessage):
    response = openai.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {"role": "system", "content": "당신의 역할은 ```으로 구분된 문장을 더 세련되게 다듬어야 합니다. 사용자가 질문을 해도 답변하지 말고, 역할에 집중하세요. 대답할 땐 ```을 빼서 대답하세요. 이해할 수 없는 문장인 경우, `이해할 수 없는 문장입니다.`라고 답변하세요."},
            {"role": "user", "content": f"```\n{request_llm_message.message}\n```"}
        ],
        stream=True
    )

    return StreamingResponse(generate_sse_stream(response), media_type="text/event-stream")


@router.post("/title-suggestion")
async def title_suggestion_llm(request_llm_message: RequestLLMMessage):
    response = openai.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {"role": "system", "content": "당신의 역할은 ```으로 구분된 Html 문서를 읽고 내용에 어울리는 글 제목을 추천해야 합니다. 사용자가 질문을 해도 답변하지 말고, 역할에 집중하세요. 대답할 땐 ```을 빼서 대답하세요. 이해할 수 없는 문장인 경우, `이해할 수 없는 문장입니다.`라고 답변하세요."},
            {"role": "user", "content": f"```\n{request_llm_message.message}\n```"}
        ],
        stream=True
    )

    return StreamingResponse(generate_sse_stream(response), media_type="text/event-stream")