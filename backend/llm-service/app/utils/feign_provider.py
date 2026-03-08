import logging
import re
from collections import defaultdict

import requests
from py_eureka_client import eureka_client
from py_eureka_client.eureka_basic import Instance


logger = logging.getLogger(__name__)


class FeignProvider:
    round_robin_map = defaultdict(int)

    def __init__(self, service_name: str):
        self.service_name = service_name

    async def __get_instance(self) -> Instance:
        app_info = await eureka_client.get_application("http://discovery-service:8761/eureka", self.service_name)
        instance_id = FeignProvider.round_robin_map[self.service_name]

        if instance_id >= len(app_info.instances):
            FeignProvider.round_robin_map[self.service_name] = 0

        instances = app_info.instances[instance_id]
        FeignProvider.round_robin_map[self.service_name] += 1

        return instances

    @staticmethod
    def __create_target_url(instance: Instance, path: str) -> str:
        if path.startswith("/"):
            RuntimeError("Path should not start with /")
        return f"http://{instance.hostName}:{instance.port.port}/{path}"

    async def aget(self, path: str):
        instance = await self.__get_instance()
        logger.info(f"instance : {instance}")
        target_url = self.__create_target_url(instance, path)
        try:
            response = requests.get(target_url)
            return response
        except Exception as e:
            logger.error(f"GET {self.service_name}/{path} URI로 요청을 보내는 데 실패했습니다. {e}")

    async def apost(self, path: str, **kwargs):
        # kwargs: json data
        instance = await self.__get_instance()
        target_url = self.__create_target_url(instance, path)
        try:
            response = requests.post(target_url, json=kwargs)
            return response
        except Exception as e:
            logger.error(f"POST {self.service_name}/{path} URI로 요청을 보내는 데 실패했습니다. {e}")