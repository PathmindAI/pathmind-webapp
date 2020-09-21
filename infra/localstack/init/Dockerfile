FROM azul/zulu-openjdk-alpine:11

RUN apk add --no-cache \
        maven \
        python3 \
        py3-pip \
    && pip3 install --upgrade pip \
    && pip3 install \
        awscli \
    && rm -rf /var/cache/apk/*

CMD ["sh","/script/run.sh"]
