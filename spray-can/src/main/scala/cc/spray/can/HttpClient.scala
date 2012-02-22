/*
 * Copyright (C) 2011, 2012 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.spray.can

import config.HttpClientConfig
import cc.spray.io._

/**
 * Reacts to [[cc.spray.can.Connect]] messages by establishing a connection to the remote host. If there is an error
 * the sender receives either an [[cc.spray.can.HttpClientException]].
 * If the connection has been established successfully a new actor is spun up for the connection, which replies to the
 * sender of the [[cc.spray.can.Connect]] message with a [[cc.spray.can.Connected]] message.
 *
 * You can then send [[cc.spray.can.model.HttpRequestPart]] instances to the connection actor, which are going to be
 * replied to with [[cc.spray.can.model.HttpResponsePart]] messages (or [[cc.spray.can.HttpClientException]] instances
 * in case of errors).
 */
class HttpClient(config: HttpClientConfig)
                (ioWorker: IoWorker = new IoWorker(config))
                extends IoClient(ioWorker) with ConnectionActors {


  protected lazy val pipelines = (
    ClientFrontend(log)
    ~> RequestRendering(config.userAgentHeader)
    ~> ResponseParsing(config, log)
    ~> ConnectionTimeoutSupport(config)
  )
}

object HttpClient extends IoClientApi {

  ////////////// COMMANDS //////////////
  // HttpRequestParts

  ////////////// EVENTS //////////////
  // HttpResponseParts

}

