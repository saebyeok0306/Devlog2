from pydantic import BaseModel


class RequestLLMMessage(BaseModel):
    message: str