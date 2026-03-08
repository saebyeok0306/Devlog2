from .llm_controller import router as llm_router
from .test_controller import router as test_router

api_routers = [llm_router, test_router]
