# Filename: Dockerfile 
FROM ubuntu:18.04
MAINTAINER abhijithanandakrishnan <abhijithananthan@gmail.com>

ARG user=abhi
ARG group=abhi
ARG uid=1000
ARG gid=1000
ARG http_port=8000
ARG agent_port=50000
ARG ABHI_HOME=/var/abhi_home

ENV ABHI_HOME $ABHI_HOME
ENV ABHI_SLAVE_AGENT_PORT ${agent_port}

RUN mkdir -p $ABHI_HOME \
  && chown ${uid}:${gid} $ABHI_HOME \
  && groupadd -g ${gid} ${group} \
  && useradd -d "$ABHI_HOME" -u ${uid} -g ${gid} -m -s /bin/bash ${user}

RUN apt-get update && \
      apt-get -y install sudo
RUN adduser abhi sudo

VOLUME $ABHI_HOME
WORKDIR $ABHI_HOME
USER ${user}
COPY ./* ./

EXPOSE 8000

CMD ["echo", "Setup Complete"]
