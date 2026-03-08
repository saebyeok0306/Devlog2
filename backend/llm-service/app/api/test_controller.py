import json
import logging

from fastapi import APIRouter
from starlette.responses import JSONResponse

from app.utils import FeignProvider

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)
router = APIRouter()


@router.post("/test")
async def test():
    provider = FeignProvider("main-service")

    response = await provider.aget("posts")
    data = json.loads(response.text)
    return JSONResponse(content=data)
